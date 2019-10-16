package maki325.opencl;

import com.sun.istack.internal.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.opencl.CLContextCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedHashMap;

import static maki325.opencl.InfoUtil.checkCLError;
import static maki325.opencl.InfoUtil.getDeviceInfoPointer;
import static org.lwjgl.opencl.CL12.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class OpenCL {

    //Buffer to put in the error code if something fails
    private IntBuffer errcode_ret;

    private long clPlatform;
    private CLCapabilities clPlatformCapabilities;

    private long clDevice;
    private CLContextCallback clContextCB;
    private long clContext;
    private long clQueue;

    private LinkedHashMap<Integer, Long> positionAddressMap;
    private LinkedHashMap<Integer, Object> positionValueMap;
    private LinkedHashMap<Integer, Integer> resultsSize;

    private String programSource;
    private long time, executionTime;

    public OpenCL(String programSource) {
        this.programSource = programSource;

        positionAddressMap = new LinkedHashMap<>();
        positionValueMap = new LinkedHashMap<>();
        resultsSize = new LinkedHashMap<>();
        initializeCL();
    }

    public OpenCL loadFloatArray(float[] array, int position, MemoryType memoryType) {
        return loadFloatArray(array, position, memoryType.flags);
    }

    public OpenCL loadFloatArray(float[] array, int position, long flags) {
        int[] error = new int[1];
        CL10.clCreateBuffer(clContext, flags, array, error);
        long address = ((flags & CL10.CL_MEM_WRITE_ONLY) == CL10.CL_MEM_WRITE_ONLY) ? CL10.clCreateBuffer(clContext, flags, array, error) : CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, array.length * 4, errcode_ret);
        if(((flags & CL10.CL_MEM_WRITE_ONLY) == CL10.CL_MEM_WRITE_ONLY)) {
            errcode_ret.put(0, error[0]);
        }
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, array.length);
        }
        return this;
    }

    public OpenCL loadFloatBuffer(FloatBuffer buffer, int position, MemoryType memoryType) {
        return loadFloatBuffer(buffer, position, memoryType.flags);
    }

    public OpenCL loadFloatBuffer(FloatBuffer buffer, int position, long flags) {
        buffer.rewind();
        long address = ((flags & CL10.CL_MEM_WRITE_ONLY) == CL10.CL_MEM_WRITE_ONLY) ? CL10.clCreateBuffer(clContext, flags, buffer, errcode_ret) : CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, buffer.capacity() * 4, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, buffer.capacity());
        }
        return this;
    }

    public OpenCL loadFloatBuffer(int size, int position, MemoryType memoryType) {
        return loadFloatBuffer(size, position, memoryType.flags);
    }

    public OpenCL loadFloatBuffer(int size, int position, long flags) {
        long address = CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, size * 4, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, size);
        }
        return this;
    }

    public OpenCL loadFloatArray(int size, int position, MemoryType memoryType) {
        return loadFloatArray(size, position, memoryType.flags);
    }

    public OpenCL loadFloatArray(int size, int position, long flags) {
        long address = CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, size * 4, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, size);
        }
        return this;
    }

    /**
     *
     */

    public OpenCL loadInt(int value, int position) {
        positionValueMap.put(position, value);
        return this;
    }

    public OpenCL loadIntArray(int[] array, int position, MemoryType memoryType) {
        return loadIntArray(array, position, memoryType.flags);
    }

    public OpenCL loadIntArray(int[] array, int position, long flags) {
        int[] error = new int[1];
        CL10.clCreateBuffer(clContext, flags, array, error);
        long address = ((flags & CL10.CL_MEM_WRITE_ONLY) == CL10.CL_MEM_WRITE_ONLY) ? CL10.clCreateBuffer(clContext, flags, array, error) : CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, array.length * 4, errcode_ret);
        if(((flags & CL10.CL_MEM_WRITE_ONLY) == CL10.CL_MEM_WRITE_ONLY)) {
            errcode_ret.put(0, error[0]);
        }
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, array.length);
        }
        return this;
    }

    public OpenCL loadIntArray(int size, int position, MemoryType memoryType) {
        return loadIntArray(size, position, memoryType.flags);
    }

    public OpenCL loadIntArray(int size, int position, long flags) {
        long address = CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, size * 4, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, size);
        }
        return this;
    }

    public OpenCL loadIntBuffer(IntBuffer buffer, int position, MemoryType memoryType) {
        return loadIntBuffer(buffer, position, memoryType.flags);
    }

    public OpenCL loadIntBuffer(IntBuffer buffer, int position, long flags) {
        buffer.rewind();
        long address = ((flags & CL10.CL_MEM_WRITE_ONLY) == CL10.CL_MEM_WRITE_ONLY) ? CL10.clCreateBuffer(clContext, flags, buffer, errcode_ret) : CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, buffer.capacity() * 4, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, buffer.capacity());
        }
        return this;
    }

    public OpenCL loadIntBuffer(int size, int position, MemoryType memoryType) {
        return loadFloatBuffer(size, position, memoryType.flags);
    }

    public OpenCL loadIntBuffer(int size, int position, long flags) {
        long address = CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, size * 4, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.put(position, address);
        if ((flags & CL10.CL_MEM_READ_ONLY) == CL10.CL_MEM_READ_ONLY) {
            resultsSize.put(position, size);
        }
        return this;
    }

    /**
     *
     */

    public FloatBuffer getResult(int position, @Nullable FloatBuffer resultBuff) {
        resultBuff = BufferUtils.createFloatBuffer(resultsSize.get(position));

        CL10.clEnqueueReadBuffer(clQueue, positionAddressMap.get(position), true, 0, resultBuff, null, null);
        return resultBuff;
    }

    public int[] getResult(int position, int[] resultArray) {
        resultArray = new int[resultsSize.get(position)];

        CL10.clEnqueueReadBuffer(clQueue, positionAddressMap.get(position), true, 0, resultArray, null, null);
        return resultArray;
    }

    public float[] getResult(int position, float[] resultArray) {
        resultArray = new float[resultsSize.get(position)];

        CL10.clEnqueueReadBuffer(clQueue, positionAddressMap.get(position), true, 0, resultArray, null, null);
        return resultArray;
    }

    public IntBuffer getResult(int position, IntBuffer resultBuff) {
        resultBuff = BufferUtils.createIntBuffer(resultsSize.get(position));

        CL10.clEnqueueReadBuffer(clQueue, positionAddressMap.get(position), true, 0, resultBuff, null, null);
        return resultBuff;
    }

    public void run(String kernel) {
        long programID = CL10.clCreateProgramWithSource(clContext, this.programSource, errcode_ret);
        int errcode = clBuildProgram(programID, clDevice, "", null, NULL);
        checkCLError(errcode);

        long clKernel = clCreateKernel(programID, kernel, errcode_ret);
        checkCLError(errcode_ret);

        positionAddressMap.forEach((position, address) -> {
            clSetKernelArg1p(clKernel, position, address);
        });

        positionValueMap.forEach((position, value) -> {
            if(value instanceof Integer) {
                clSetKernelArg1i(clKernel, position, (int) value);
            } else if(value instanceof Float) {
                clSetKernelArg1f(clKernel, position, (float) value);
            } else if(value instanceof Double) {
                clSetKernelArg1d(clKernel, position, (double) value);
            }
        });

        final int dimensions = 1;
        PointerBuffer globalWorkSize = BufferUtils.createPointerBuffer(dimensions); // In here we put
        // the total number
        // of work items we
        // want in each
        // dimension.
        globalWorkSize.put(0, getDeviceInfoPointer(clDevice, CL_DEVICE_MAX_WORK_GROUP_SIZE)); // Size is a variable we defined a while back showing how many
        // elements are in our arrays.

        time = System.nanoTime();
        errcode = clEnqueueNDRangeKernel(clQueue, clKernel, dimensions, null, globalWorkSize, null, null, null);
        checkCLError(errcode);

        CL10.clFinish(clQueue);
        executionTime = System.nanoTime() - time;
    }

    private void initializeCL() {
        errcode_ret = BufferUtils.createIntBuffer(1);

        // Get the first available platform
        try (MemoryStack stack = stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            checkCLError(clGetPlatformIDs(null, pi));
            if (pi.get(0) == 0) {
                throw new IllegalStateException("No OpenCL platforms found.");
            }

            PointerBuffer platformIDs = stack.mallocPointer(pi.get(0));
            checkCLError(clGetPlatformIDs(platformIDs, (IntBuffer) null));

            for (int i = 0; i < platformIDs.capacity() && i == 0; i++) {
                long platform = platformIDs.get(i);
                clPlatformCapabilities = CL.createPlatformCapabilities(platform);
                clPlatform = platform;
            }
        }


        clDevice = getDevice(clPlatform, clPlatformCapabilities, CL_DEVICE_TYPE_GPU);

        // Create the context
        PointerBuffer ctxProps = BufferUtils.createPointerBuffer(7);
        ctxProps.put(CL_CONTEXT_PLATFORM).put(clPlatform).put(NULL).flip();

        clContext = clCreateContext(ctxProps, clDevice,
                clContextCB = CLContextCallback.create((errinfo, private_info, cb, user_data) -> System.out.printf("cl_context_callback\n\tInfo: %s", memUTF8(errinfo))),
                NULL, errcode_ret);

        // create command queue
        clQueue = clCreateCommandQueue(clContext, clDevice, NULL, errcode_ret);
        checkCLError(errcode_ret);
    }

    private static long getDevice(long platform, CLCapabilities platformCaps, int deviceType) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            checkCLError(clGetDeviceIDs(platform, deviceType, null, pi));

            PointerBuffer devices = stack.mallocPointer(pi.get(0));
            checkCLError(clGetDeviceIDs(platform, deviceType, devices, (IntBuffer) null));

            for (int i = 0; i < devices.capacity(); i++) {
                long device = devices.get(i);

                CLCapabilities caps = CL.createDeviceCapabilities(device, platformCaps);
                if (!(caps.cl_khr_gl_sharing || caps.cl_APPLE_gl_sharing)) {
                    continue;
                }

                return device;
            }
        }

        return NULL;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public enum MemoryType {

        READ(CL10.CL_MEM_READ_ONLY), WRITE(CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR), READ_AND_WRITE(CL10.CL_MEM_READ_ONLY | CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR);

        long flags;

        MemoryType(long flags) {
            this.flags = flags;
        }

    }

}
