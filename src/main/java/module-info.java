



//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya
//                           wilmer 2019/05/25
//
//--------------------------------------------------------------------------------
/**
 * - This library module does not contain the executable file and the main method.
 *
 * - pure SL4J methods are used for logging, this way the logger implementation of the client using the library will be picked up automatically
 *      logback used only for testing library
 */


module life.expert {

exports life.expert.common.async;
exports life.expert.common.base;
exports life.expert.common.function;
exports life.expert.common.io;
exports life.expert.common.reactivestreams;
exports life.expert.common.collect;
exports life.expert.common.graph;
exports life.expert.value.string;
exports life.expert.value.numeric.amount;
exports life.expert.value.numeric.context;
exports life.expert.value.numeric.operators;
exports life.expert.value.numeric.unit;
exports life.expert.value.numeric.utils;

//exports himalaya.extensions.java.lang.String;

// Mandatory
//requires manifold.all;  // the manifold-all jar file (or a set of constituent core Manifold jars)
// Mandatory for **Java 11** or later in MULTI-MODULE MODE
//requires jdk.unsupported; // As a convenience Manifold uses internal Java APIs to make module setup easier for you
//requires java.xml.bind;



requires java.logging;
requires static lombok;
//requires io.vavr.match;
//requires com.google.common;
//requires org.apache.commons.lang3;

//requires cyclops;

//requires gson;

///requires org.jetbrains.annotations;
//requires error.prone.annotations;
requires transitive org.reactivestreams;
requires transitive reactor.core;
requires transitive reactor.extra;


requires org.slf4j;
requires transitive org.jetbrains.annotations;

requires com.google.common;
requires org.apache.commons.lang3;

requires transitive io.vavr;
requires static io.vavr.match;

requires gson;
requires transitive cyclops;
requires error.prone.annotations;
requires org.apache.commons.io;


//requires manifold.ext;


}