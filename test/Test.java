import maki325.opencl.OpenCL;

public class Test {

    public static void main(String[] args) {
        boolean printResults = false;
        int size = Integer.MAX_VALUE / 7;
        String sumProgramSource =
                "kernel void sum(global const int* a, global const int* b, global float* result, int const size) {"
                        + "  const int itemId = get_global_id(0);" + "  if(itemId < size) {"
                        + "    result[itemId] = pow(sqrt(a[itemId]*1.0f) + sqrt(b[itemId]*1.0f), 2);" + "  }" + "}";

        if(args.length != 0) {
            for(int i = 0;i < args.length;i++) {
                String arg = args[i];
                if(arg.equalsIgnoreCase("--print-results")) {
                    printResults = true;
                }
                if(arg.equalsIgnoreCase("--size")) {
                    if(args.length == i + 1) {
                        throw new Error("The size wasn't specified.");
                    } else {
                        try {
                            size = Integer.parseInt(args[i + 1]);
                            if(size <= 0) throw new Error("The size is bellow 0!");
                            if(size > Integer.MAX_VALUE / 7) {
                                size = Integer.MAX_VALUE / 7;
                                System.out.println("The size was maxed out on " + Integer.MAX_VALUE / 7);
                            }
                        } catch (Exception e) {
                            throw new Error("The size wasn't specified.");
                        }
                    }
                }
            }
        }

        OpenCL program = new OpenCL(sumProgramSource);
        System.out.println("Size: " + size);

        System.out.println("Loading Array A!");
        int[] aArray = new int[size], bArray = new int[size];
        for (int i = 0; i < size; i++) {
            aArray[i] = i;
        }
        program.loadIntArray(aArray, 0, OpenCL.MemoryType.WRITE);
        System.out.println("Array A Loaded!");

        System.out.println("Loading Array B!");
        for (int j = 0, i = size - 1; j < size; j++, i--) {
            bArray[j] = i;
        }
        program.loadIntArray(bArray, 1, OpenCL.MemoryType.WRITE);
        System.out.println("Array B Loaded!");
        program.loadFloatArray(size, 2, OpenCL.MemoryType.READ)
                .loadInt(size, 3);

        System.out.println("Running program!");
        program.run("sum" /* The name of the kernel in the .CL file. */);
        System.out.println("Program finished! Execution time: " + program.getExecutionTime());

        if(printResults) {
            System.out.println("Getting results!");
            float[] resultArray = null;
            resultArray = program.getResult(2, resultArray);
            System.out.println("Results returned!");

            for (int i = 0; i < resultArray.length; i++) {
                System.out.println("Result at " + i + " = " + resultArray[i]);
            }
        }

    }

}
