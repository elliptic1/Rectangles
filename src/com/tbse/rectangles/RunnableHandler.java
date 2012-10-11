package com.tbse.rectangles;

import java.util.ArrayList;

import org.anddev.andengine.engine.handler.IUpdateHandler;

public class RunnableHandler implements IUpdateHandler {

	private final ArrayList<Runnable> mRunnables = new ArrayList<Runnable>();

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		int runnableCount = this.mRunnables.size();
		for (int i = 0; i < runnableCount; i++) {
			this.mRunnables.remove(0).run();
		}
	}

	@Override
	public void reset() {
		this.mRunnables.clear();
	}

	public void postRunnable(final Runnable pRunnable) {
		this.mRunnables.add(pRunnable);
	}

}
