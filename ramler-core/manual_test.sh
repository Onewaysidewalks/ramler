clear

gradle clean shadowJar

/c/Program\ Files/Java/jdk1.8.0_40/bin/javac.exe \
-J-Xdebug -J-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 \
ramler-core/src/test/java/ninja/onewaysidewalks/ramler/core/TestInterface.java \
-classpath ramler-core/build/libs/ramler-core-all.jar \
-processorpath ramler-core/build/libs/ramler-core-all.jar \
-processor ninja.onewaysidewalks.ramler.core.RamlerProcessor \
-Aninja.onewaysidewalks.ramler.config='{"basePackagesForReflection":["ninja.onewaysidewalks"],"supportedAnnotationTypes":["lombok.Getter"]}' \
-implicit:class