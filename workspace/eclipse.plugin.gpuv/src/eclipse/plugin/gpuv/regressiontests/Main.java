package eclipse.plugin.gpuv.regressiontests;

import math.MyMath;
import eclipse.plugin.gpuv.*;

public class Main {
  public static void main(String[] args) {
    MyMath math = new MyMath();
    System.out.println("Result is: " + math.multi(5, 10));
  }
} 