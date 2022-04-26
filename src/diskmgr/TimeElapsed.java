package diskmgr;

public abstract class TimeElapsed {
    private String taskName;
    
    public TimeElapsed(String taskName) {
        this.taskName = taskName;
    }
    
    public abstract void doMethod() throws Exception;
    
    public void run() throws Exception {
        long startTime = System.currentTimeMillis();
        doMethod();
        System.out.println(String.format("Time taken to Perform %s Task: %d ms",taskName,(System.currentTimeMillis()-startTime)));
    }
}
