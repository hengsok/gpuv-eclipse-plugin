#An Eclipse IDE Plugin for GPUVerify
===================

This is a project done by a group of 5 Imperial College students which made the life of OpenCL developers easier by building an IDE for them. Included is the ability to run GPUVerify, a project by Alastair F. Donaldson(Leader), Adam Betts,  Jeroen Ketema, Peter Collingbourne.

##Introduction

###What is GPUVerify?
GPUVerify is an on-going research project on analysing GPU kernels written in OpenCL and CUDA, being carried out in department of computing at Imperial College London. It has been in progress for about a year and is currently led by Dr. Alastair Donaldson, our supervisor. GPUVerify lacks an appropriate development environment for researchers and other users. Our solution, GPUVerify plugin for Eclipse, provides a powerful integrated development environment for GPUVerify project in which users can write OpenCL code and run analysis with GPUVerify. This will serve to assist the development through visual feedback and easy-to-use OpenCL editor that we have developed, and invoke GPUVerify analysis tool with comprehensive option configuration with a few mouse-clicks. The OpenCL editor is rich in features, providing syntax highlighting, content assistant for function names and keywords, error detection, and connection with GPUVerify tool so that analysis results of possible problems (e.g. race conditions) are also highlighted on specific lines of codes. The plugin also allows configuring and running GPUVerify analysis with a click of a button, or even automatically on each save of the file. All these features can conveniently be installed on both Linux and Windows machines by simply extracting the contents of the published zip file into dropin folder of Eclipse installation directory. The plugin will definitely enhance the productivity of the project greatly, with an insignificant setup cost.

###What have we done to contribute to GPUVerify project?
Although GPUVerify tool is useful in verifying the kernels written in OpenCL and CUDA, there is currently no integrated development environment (IDE) for the users and developers of the tool. This means that the only way to invoke the tool is via the command line (i.e. users need to configure the options that they want to pass to GPUVerify tool by typing out the option names and target file names whenever they want to run analysis on OpenCL or CUDA script with GPUVerify), and the target codes to be verified must be written and edited in a basic text editor. Since the tool is made for OpenCL and CUDA developers who should not be assumed to know in detail about makes it inconvenient and inefficient to use. Our team of five had the privilege of developing a unified integrated development environment as a plugin for Eclipse, which provides rich text editing functions for OpenCL (including content assistant and auto-suggestion of built-in OpenCL function names and keywords, syntax highlighting, function templates and error annotations) and fully integrated functionality for configuring GPUVerify tool using a configuration box and running analysis seamlessly in the background. The result output from the GPUVerify tool will be fetched back by the IDE and an error parser will be invoked automatically so that any identified problems can be highlighted in the Problem View of Eclipse IDE. All of the features works both on Linux and Windows since we have put in a fair amount of consideration of the implementation difference for both operating systems.

We started by creating an editor for OpenCL, which performs syntax highlighting, auto- completion of keywords and function names, description box and parameter indications for OpenCL built-in functions, and the error annotations (underlining syntax errors) for OpenCL language. Similarly to some Eclipse plugins that support specific programming languages, such as C++, we aimed to create a plugin that provides rich supportive functionalities to aid OpenCL developers. Implementation details for the features will be explained in the next section.



##License

(c) Copyright 2013 Heng Sok (hs4110), Inhyeok Kim (ik610), Yuxiang Zhou (yz4009), Myung Lee (msl09), Hin Cheng (hfc10)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

http://www.gnu.org/licenses/
