all::

ifndef VERBOSE
  Verb = @
endif

utils/prepare-builtins.o: ./utils/prepare-builtins.cpp
	@echo CXX utils/prepare-builtins.o
	$(Verb) c++ -MMD -MF utils/prepare-builtins.o.d -I/vol/multicore/afd/llvm/include -I/vol/multicore/afd/llvm-build/include  -D_DEBUG -D_GNU_SOURCE -D__STDC_CONSTANT_MACROS -D__STDC_FORMAT_MACROS -D__STDC_LIMIT_MACROS -O3 -fomit-frame-pointer -fvisibility-inlines-hidden -fno-exceptions -fno-rtti -fPIC -Woverloaded-virtual -Wcast-qual -fno-exceptions -fno-rtti -c -o utils/prepare-builtins.o ./utils/prepare-builtins.cpp
-include utils/prepare-builtins.o.d

utils/prepare-builtins: utils/prepare-builtins.o
	@echo LINK utils/prepare-builtins
	$(Verb) c++ -o utils/prepare-builtins utils/prepare-builtins.o -L/vol/multicore/afd/llvm-build/Release+Asserts/lib  -lpthread -ldl -lm  -lLLVMBitWriter -lLLVMBitReader -lLLVMCore -lLLVMSupport -ldl

nvptx--bugle/lib/integer/add_sat.ll.bc: ./ptx/lib/integer/add_sat.ll
	@echo LLVM-AS nvptx--bugle/lib/integer/add_sat.ll.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/llvm-as -o nvptx--bugle/lib/integer/add_sat.ll.bc ./ptx/lib/integer/add_sat.ll

nvptx--bugle/lib/integer/sub_sat.ll.bc: ./ptx/lib/integer/sub_sat.ll
	@echo LLVM-AS nvptx--bugle/lib/integer/sub_sat.ll.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/llvm-as -o nvptx--bugle/lib/integer/sub_sat.ll.bc ./ptx/lib/integer/sub_sat.ll

nvptx--bugle/lib/convert.cl.bc: ./generic/lib/convert.cl
	@echo LLVM-CC nvptx--bugle/lib/convert.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/convert.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/convert.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/convert.cl.bc ./generic/lib/convert.cl
-include nvptx--bugle/lib/convert.cl.bc.d

nvptx--bugle/lib/geometric/cross.cl.bc: ./generic/lib/geometric/cross.cl
	@echo LLVM-CC nvptx--bugle/lib/geometric/cross.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/geometric/cross.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/geometric/cross.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/geometric/cross.cl.bc ./generic/lib/geometric/cross.cl
-include nvptx--bugle/lib/geometric/cross.cl.bc.d

nvptx--bugle/lib/geometric/dot.cl.bc: ./generic/lib/geometric/dot.cl
	@echo LLVM-CC nvptx--bugle/lib/geometric/dot.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/geometric/dot.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/geometric/dot.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/geometric/dot.cl.bc ./generic/lib/geometric/dot.cl
-include nvptx--bugle/lib/geometric/dot.cl.bc.d

nvptx--bugle/lib/geometric/length.cl.bc: ./generic/lib/geometric/length.cl
	@echo LLVM-CC nvptx--bugle/lib/geometric/length.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/geometric/length.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/geometric/length.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/geometric/length.cl.bc ./generic/lib/geometric/length.cl
-include nvptx--bugle/lib/geometric/length.cl.bc.d

nvptx--bugle/lib/geometric/normalize.cl.bc: ./generic/lib/geometric/normalize.cl
	@echo LLVM-CC nvptx--bugle/lib/geometric/normalize.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/geometric/normalize.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/geometric/normalize.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/geometric/normalize.cl.bc ./generic/lib/geometric/normalize.cl
-include nvptx--bugle/lib/geometric/normalize.cl.bc.d

nvptx--bugle/lib/integer/abs.cl.bc: ./generic/lib/integer/abs.cl
	@echo LLVM-CC nvptx--bugle/lib/integer/abs.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/integer/abs.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/integer/abs.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/integer/abs.cl.bc ./generic/lib/integer/abs.cl
-include nvptx--bugle/lib/integer/abs.cl.bc.d

nvptx--bugle/lib/integer/add_sat.cl.bc: ./generic/lib/integer/add_sat.cl
	@echo LLVM-CC nvptx--bugle/lib/integer/add_sat.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/integer/add_sat.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/integer/add_sat.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/integer/add_sat.cl.bc ./generic/lib/integer/add_sat.cl
-include nvptx--bugle/lib/integer/add_sat.cl.bc.d

nvptx--bugle/lib/integer/add_sat_impl.ll.bc: ./generic/lib/integer/add_sat_impl.ll
	@echo LLVM-AS nvptx--bugle/lib/integer/add_sat_impl.ll.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/llvm-as -o nvptx--bugle/lib/integer/add_sat_impl.ll.bc ./generic/lib/integer/add_sat_impl.ll

nvptx--bugle/lib/integer/sub_sat.cl.bc: ./generic/lib/integer/sub_sat.cl
	@echo LLVM-CC nvptx--bugle/lib/integer/sub_sat.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/integer/sub_sat.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/integer/sub_sat.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/integer/sub_sat.cl.bc ./generic/lib/integer/sub_sat.cl
-include nvptx--bugle/lib/integer/sub_sat.cl.bc.d

nvptx--bugle/lib/integer/sub_sat_impl.ll.bc: ./generic/lib/integer/sub_sat_impl.ll
	@echo LLVM-AS nvptx--bugle/lib/integer/sub_sat_impl.ll.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/llvm-as -o nvptx--bugle/lib/integer/sub_sat_impl.ll.bc ./generic/lib/integer/sub_sat_impl.ll

nvptx--bugle/lib/math/hypot.cl.bc: ./generic/lib/math/hypot.cl
	@echo LLVM-CC nvptx--bugle/lib/math/hypot.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/math/hypot.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/math/hypot.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/math/hypot.cl.bc ./generic/lib/math/hypot.cl
-include nvptx--bugle/lib/math/hypot.cl.bc.d

nvptx--bugle/lib/math/mad.cl.bc: ./generic/lib/math/mad.cl
	@echo LLVM-CC nvptx--bugle/lib/math/mad.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/math/mad.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/math/mad.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/math/mad.cl.bc ./generic/lib/math/mad.cl
-include nvptx--bugle/lib/math/mad.cl.bc.d

nvptx--bugle/lib/relational/any.cl.bc: ./generic/lib/relational/any.cl
	@echo LLVM-CC nvptx--bugle/lib/relational/any.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/relational/any.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/relational/any.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/relational/any.cl.bc ./generic/lib/relational/any.cl
-include nvptx--bugle/lib/relational/any.cl.bc.d

nvptx--bugle/lib/workitem/get_global_id.cl.bc: ./generic/lib/workitem/get_global_id.cl
	@echo LLVM-CC nvptx--bugle/lib/workitem/get_global_id.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/workitem/get_global_id.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/workitem/get_global_id.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/workitem/get_global_id.cl.bc ./generic/lib/workitem/get_global_id.cl
-include nvptx--bugle/lib/workitem/get_global_id.cl.bc.d

nvptx--bugle/lib/workitem/get_global_size.cl.bc: ./generic/lib/workitem/get_global_size.cl
	@echo LLVM-CC nvptx--bugle/lib/workitem/get_global_size.cl.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/clang -MMD -MF nvptx--bugle/lib/workitem/get_global_size.cl.bc.d -target nvptx--bugle -I`dirname ./generic/lib/workitem/get_global_size.cl` -I./generic/include -Dcl_clang_storage_class_specifiers -Dcl_khr_fp64 -emit-llvm -c -o nvptx--bugle/lib/workitem/get_global_size.cl.bc ./generic/lib/workitem/get_global_size.cl
-include nvptx--bugle/lib/workitem/get_global_size.cl.bc.d

nvptx--bugle/lib/builtins.link.bc: nvptx--bugle/lib/integer/add_sat.ll.bc nvptx--bugle/lib/integer/sub_sat.ll.bc nvptx--bugle/lib/convert.cl.bc nvptx--bugle/lib/geometric/cross.cl.bc nvptx--bugle/lib/geometric/dot.cl.bc nvptx--bugle/lib/geometric/length.cl.bc nvptx--bugle/lib/geometric/normalize.cl.bc nvptx--bugle/lib/integer/abs.cl.bc nvptx--bugle/lib/integer/add_sat.cl.bc nvptx--bugle/lib/integer/add_sat_impl.ll.bc nvptx--bugle/lib/integer/sub_sat.cl.bc nvptx--bugle/lib/integer/sub_sat_impl.ll.bc nvptx--bugle/lib/math/hypot.cl.bc nvptx--bugle/lib/math/mad.cl.bc nvptx--bugle/lib/relational/any.cl.bc nvptx--bugle/lib/workitem/get_global_id.cl.bc nvptx--bugle/lib/workitem/get_global_size.cl.bc
	@echo LLVM-LINK nvptx--bugle/lib/builtins.link.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/llvm-link -o nvptx--bugle/lib/builtins.link.bc nvptx--bugle/lib/integer/add_sat.ll.bc nvptx--bugle/lib/integer/sub_sat.ll.bc nvptx--bugle/lib/convert.cl.bc nvptx--bugle/lib/geometric/cross.cl.bc nvptx--bugle/lib/geometric/dot.cl.bc nvptx--bugle/lib/geometric/length.cl.bc nvptx--bugle/lib/geometric/normalize.cl.bc nvptx--bugle/lib/integer/abs.cl.bc nvptx--bugle/lib/integer/add_sat.cl.bc nvptx--bugle/lib/integer/add_sat_impl.ll.bc nvptx--bugle/lib/integer/sub_sat.cl.bc nvptx--bugle/lib/integer/sub_sat_impl.ll.bc nvptx--bugle/lib/math/hypot.cl.bc nvptx--bugle/lib/math/mad.cl.bc nvptx--bugle/lib/relational/any.cl.bc nvptx--bugle/lib/workitem/get_global_id.cl.bc nvptx--bugle/lib/workitem/get_global_size.cl.bc

nvptx--bugle/lib/builtins.opt.bc: nvptx--bugle/lib/builtins.link.bc
	@echo OPT nvptx--bugle/lib/builtins.opt.bc
	$(Verb) /vol/multicore/afd/llvm-build/Release+Asserts/bin/opt -O3 -o nvptx--bugle/lib/builtins.opt.bc nvptx--bugle/lib/builtins.link.bc

nvptx--bugle/lib/builtins.bc: nvptx--bugle/lib/builtins.opt.bc utils/prepare-builtins
	@echo PREPARE-BUILTINS nvptx--bugle/lib/builtins.bc
	$(Verb) utils/prepare-builtins -o nvptx--bugle/lib/builtins.bc nvptx--bugle/lib/builtins.opt.bc

Makefile: ./configure.py ./build/metabuild.py ./build/ninja_syntax.py ./ptx/lib/SOURCES ./generic/lib/SOURCES
	@echo CONFIGURE
	$(Verb) ./configure.py nvptx--bugle --with-llvm-config=/vol/multicore/afd/llvm-build/Release+Asserts/bin/llvm-config

all:: nvptx--bugle/lib/builtins.bc

clean: 
	rm -f utils/prepare-builtins.o utils/prepare-builtins nvptx--bugle/lib/integer/add_sat.ll.bc nvptx--bugle/lib/integer/sub_sat.ll.bc nvptx--bugle/lib/convert.cl.bc nvptx--bugle/lib/geometric/cross.cl.bc nvptx--bugle/lib/geometric/dot.cl.bc nvptx--bugle/lib/geometric/length.cl.bc nvptx--bugle/lib/geometric/normalize.cl.bc nvptx--bugle/lib/integer/abs.cl.bc nvptx--bugle/lib/integer/add_sat.cl.bc nvptx--bugle/lib/integer/add_sat_impl.ll.bc nvptx--bugle/lib/integer/sub_sat.cl.bc nvptx--bugle/lib/integer/sub_sat_impl.ll.bc nvptx--bugle/lib/math/hypot.cl.bc nvptx--bugle/lib/math/mad.cl.bc nvptx--bugle/lib/relational/any.cl.bc nvptx--bugle/lib/workitem/get_global_id.cl.bc nvptx--bugle/lib/workitem/get_global_size.cl.bc nvptx--bugle/lib/builtins.link.bc nvptx--bugle/lib/builtins.opt.bc nvptx--bugle/lib/builtins.bc

distclean: clean
	rm -f Makefile
