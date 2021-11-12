package urlImage.threads;

public class LoadingThread implements Runnable {

    @Override
    public void run() {
        System.out.print("Изображение скачивается");
        while (true) {
            try {
                Thread.sleep(300);
                System.out.print(".");
                Thread.sleep(300);
                System.out.print(".");
                Thread.sleep(300);
                System.out.print(".");
                Thread.sleep(300);
                System.out.print("\b\b\b");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
