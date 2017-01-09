package cqupt.counter;

/**
 * 计数器接口
 */

public interface IConterService {
    public void startCounter(int initVal, IConterCallback callback);
    public void stopCounter();
}
