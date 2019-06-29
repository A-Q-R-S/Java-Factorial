class Timer {
	long timer 		= -1;
	long tempTimer 	= -1;
	
	public Timer () {
		timer = 0;
		this.start();
	}
	
	void start() {
		tempTimer = System.currentTimeMillis();
	}
	
	void stop() {
		timer = this.getCurrent();
	}
	
	long getCurrent() {
		return this.timer + System.currentTimeMillis() - tempTimer;
	}
}