package com.anbang.p2p.cqrs.c.service.disruptor;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.anbang.p2p.conf.FilePathConfig;
import com.highto.framework.ddd.CRPair;
import com.highto.framework.ddd.Command;
import com.highto.framework.disruptor.Handler;
import com.highto.framework.disruptor.event.CommandEvent;
import com.highto.framework.file.JournalFile;
import com.highto.framework.nio.ByteBufferAble;
import com.highto.framework.nio.ByteBufferSerializer;
import com.highto.framework.nio.ReuseByteBuffer;
import com.lmax.disruptor.EventHandler;

/**
 * 记录命令事件，用于命令事件replay。<br/>
 * 同时也负责生成snapshot。
 *
 * @author zheng chengdong
 */
public class ProcessCoreCommandEventHandler implements EventHandler<CommandEvent> {
	private CoreSnapshotFactory coreSnapshotFactory;
	private SnapshotJsonUtil snapshotJsonUtil;

	private String snapshotFileBasePath;

	private String jFileBasePath;

	private JournalFile jFile;

	private ReuseByteBuffer reuseByteBuffer;

	private FileUtil fileUtil = new FileUtil();

	public ProcessCoreCommandEventHandler(CoreSnapshotFactory coreSnapshotFactory, SnapshotJsonUtil snapshotJsonUtil,
			FilePathConfig filePathConfig) {
		this.coreSnapshotFactory = coreSnapshotFactory;
		this.snapshotJsonUtil = snapshotJsonUtil;
		snapshotFileBasePath = filePathConfig.getSnapshotFileBasePath();
		jFileBasePath = filePathConfig.getjFileBasePath();
	}

	@Override
	public void onEvent(CommandEvent event, long sequence, boolean endOfBatch) throws Exception {
		if (jFile == null) {

			String recentFileName = fileUtil.getRecentFileName(jFileBasePath);
			if (recentFileName == null || recentFileName.equals("")) {
				recentFileName = jFileBasePath + "/" + System.currentTimeMillis();
			}
			jFile = new JournalFile(recentFileName);
			reuseByteBuffer = new ReuseByteBuffer(ByteBuffer.allocateDirect(1024 * 1024));
		}
		if (!event.isSnapshot()) {
			Command cmd = event.getCmd();
			boolean b = false;
			if (b) {
				recordJournalFile(cmd, null);
			}
			Handler handler = event.getHandler();
			if (handler != null) {
				ByteBufferAble cmdResult = handler.handle();
				if (event.isRecordResult()) {
					recordJournalFile(cmd, cmdResult);
				} else {
					recordJournalFile(cmd, null);
				}
			}
		} else {
			try {
				saveSnapshot();
				jFile.close();
				jFile = new JournalFile(jFileBasePath + "/" + System.currentTimeMillis());
			} catch (Throwable e) {
				System.out.println("System.exit(0)  " + e.getMessage());
				System.exit(0);// 任何失败系统停机。
			}
		}
	}

	private void recordJournalFile(Command cmd, ByteBufferAble cmdResult) {
		ByteBuffer bb = reuseByteBuffer.take();
		try {
			if (cmdResult != null) {
				CRPair pair = new CRPair(cmd, cmdResult);
				ByteBufferSerializer.objToByteBuffer(pair, bb);
			} else {
				ByteBufferSerializer.objToByteBuffer(cmd, bb);
			}
			jFile.write(bb);
		} catch (Throwable e) {
			System.exit(0);// 任何失败系统停机。
		}
	}

	private void saveSnapshot() throws IOException {
		CoreSnapshot snapshoot = coreSnapshotFactory.createSnapshoot();
		snapshotJsonUtil.save(snapshotFileBasePath, snapshoot.getCreateTime() + "", snapshoot);
	}

	public String getSnapshotFileBasePath() {
		return snapshotFileBasePath;
	}

	public String getjFileBasePath() {
		return jFileBasePath;
	}

}
