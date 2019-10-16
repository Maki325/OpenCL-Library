# OpenCL-Library
Library and wrapper created on top of LWJGL OpenCL implementation.

#The test
The test file (/test/Test.java) runs a compute shader that does a mathematical equation( x = pow(sqrt(a\*1.0f) + sqrt(b\*1.0f), 2) )  for all the elements in the array with the specified size and returns the array that can be printed if the option is set.

## How to use the test
Build the jar with any IDE you want and then run the command in the directory where the file is.

If you open the project in IntelliJ Idea you can run the project with predefined size that can be changed.
```shell script
java -jar OpenCL.jar [--print-results] [--size x]
```
Arguments:
- --size x - sets the size of the buffers to x. Maximum size is <b>306 783 378</b> (which is Integer.MAX_VALUE / 7)
- --print-results - Prints the results from. Maximum length is the above mentioned maximum size