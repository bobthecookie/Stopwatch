package io.github.bobthecookie.stopwatch;

public class StopwatchPlayer implements Comparable<StopwatchPlayer>{
	private long totalTime = 0;
	private String name = "UNNAMED_PLAYER";
	
	public StopwatchPlayer(String name, long totalTime){
		this.name = name;
		this.totalTime = totalTime;
	}
	public StopwatchPlayer(String name){
		this.name = name;
		StopwatchPlayer p = StopwatchUtil.loadPlayerInfo(name);
		totalTime = p.getTotalTime();
		if(totalTime == -1)
			totalTime = 0;
	}
	public String formattedInfo(){
		String str = name;
		int[] t = formatTime();
		str += " "+t[0]+" hours "+t[1]+" minutes "+t[2]+" seconds";
		return str;
	}
	public String getName(){
		return name;
	}
	public long getTotalTime(){
		return totalTime;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setTotalTime(long totalTime){
		this.totalTime = totalTime;
	}
	public void addTime(long time){
		totalTime += time;
	}
	
	public String toString(){
		return "["+name+","+totalTime+"]";
	}
	
	public int compareTo(StopwatchPlayer p) {
		return (int)(p.getTotalTime()-totalTime);
	}
	private int[] formatTime(){
		long t = totalTime;
		if(t==-1)
			return null;
		t /= 1000;
		int h = ((int) t) / 3600;
		t -= h * 3600;
		int m = ((int) t) / 60;
		t -= m * 60;
		return new int[]{h,m,(int)t};
	}
}
