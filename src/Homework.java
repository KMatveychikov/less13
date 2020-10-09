import java.util.Arrays;


public class Homework<Static> {
    static int SIZE = 10000000;
    static int HALF_SIZE = SIZE/2;

    private static float[] createArray(int size){
        float array[] = new float[size];
        Arrays.fill(array , 1.0f);
        return array;
    }

    private static void timeMeter(Runnable action, String actionName){
        long a = System.nanoTime();
        action.run();
        long time = (System.nanoTime() - a) / 1000000;
        System.out.println("выполнение " +actionName+" заняло " + time+ " мс" );

    }
    private static float calcValue(int index, float value){
            return (float)(value * Math.sin(0.2f + index / 5) * Math.cos(0.2f + index / 5) * Math.cos(0.4f + index / 2));

    }
    private static void seqMethod(float[] array){
        seqMethod(array, 0);
    }

    private static void seqMethod(float[] array, int offset){
        for (int i = 0; i < array.length; i++) {
            float value = array[i];
            array[i] = calcValue(i+offset,value);
        }
    }

    private static Runnable parallelMethod(float[] arr){
        float[] a1 = Arrays.copyOfRange(arr,0,HALF_SIZE);
        float[] a2 = Arrays.copyOfRange(arr,HALF_SIZE,arr.length);

        Thread thread1 = new Thread(() -> seqMethod(a1,0));
        Thread thread2 = new Thread(() -> seqMethod(a2,HALF_SIZE));

        thread1.start();
        thread2.start();
            try {
                thread1.join();
                thread2.join();
            }catch (Exception e){
                e.printStackTrace();

            }
        System.arraycopy(a1, 0, arr, 0, HALF_SIZE);
        System.arraycopy(a2, 0, arr, HALF_SIZE, HALF_SIZE);

        return null;
    }

    public static void main(String[] args) {
        float[] arr1 = createArray(SIZE);
        timeMeter(() -> seqMethod(arr1), "singleMethod");

        float[] arr2 = createArray(SIZE);
        timeMeter(() -> parallelMethod(arr2), "parallelMethod");

        System.out.println(Arrays.equals(arr1,arr2));

    }
}
