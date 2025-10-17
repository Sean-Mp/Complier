How to use:

1. Firstly, we are using CUP which is a parser generator tool
2. in src you will find Main.java, SPL.cup, SPL.flex
3. SPL.cup is the parser specification file
4. the build.sh in the root is a shell script that will generate the parser and lexer code using CUP and JFlex
5. if you change anything in SPL.cup you must do the following steps:
   - rm -rf out (to remove the old generated files)
   - ./build.sh (to generate new parser and lexer files)
   - then you can run commands like:
   - java -cp "out:lib/*" Main comprehensive_test.spl (to run the comprehensive test file)

if you want to run specific parts quicker, you can run:
   - echo 'glob {} proc {} func {} main { var {} halt }' | java -cp "out:lib/*" Main
   - or something in this form
   