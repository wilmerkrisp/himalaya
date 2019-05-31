



//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya
//                           wilmer 2019/05/25
//
//--------------------------------------------------------------------------------



module life.expert.himalaya {

exports life.expert.common.async;
exports life.expert.common.base;
exports life.expert.common.function;
exports life.expert.common.io;
exports life.expert.common.reactivestreams;
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
requires org.reactivestreams;
requires reactor.core;
requires reactor.extra;


requires org.slf4j;
requires org.jetbrains.annotations;

requires com.google.common;
requires org.apache.commons.lang3;

requires io.vavr;
requires io.vavr.match;

requires gson;
requires cyclops;
requires error.prone.annotations;


//requires manifold.ext;


}