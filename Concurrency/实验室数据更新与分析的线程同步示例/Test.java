package com.vin.thread;


/**
 * created by Vintage
 */
public class Test {

    public static void main(String[] args) {
        Experiment data = new Experiment();

        // 实验助理与实验分析人员共用同一个实验
        Assistant assistant = new Assistant(data);
        Analyst analyst = new Analyst(data);
        assistant.start();
        analyst.start();
    }
}
