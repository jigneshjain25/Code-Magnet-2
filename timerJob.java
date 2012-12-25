
public class timerJob implements Runnable{
		
		Main obj;
		public timerJob(Main o) {
			obj=o;
		}
	
		public void run(){
			while(true)
			{
				obj.time++;
				obj.frame.setTitle("Code Magnet "+obj.time);
				try 
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	
}
