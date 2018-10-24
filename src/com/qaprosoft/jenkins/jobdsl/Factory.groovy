package com.qaprosoft.jenkins.jobdsl

import com.qaprosoft.Logger
import com.qaprosoft.Utils

// groovy script for initialization and execution all kind of jobdsl factories which are transfered from pipeline scanner script

import groovy.json.*

Logger logger = new Logger(this)
def slurper = new JsonSlurper()

String factoryDataMap = readFileFromWorkspace("factories.json")
logger.info("FactoryDataMap: ${JsonOutput.prettyPrint(factoryDataMap)}")
def prettyPrint = JsonOutput.prettyPrint(factoryDataMap)
println "factoryDataMap: " + prettyPrint
def factories = new HashMap(slurper.parseText(factoryDataMap))

factories.each{
	try {
		def factory = Class.forName(it.value.clazz)?.newInstance(this)
        logger.debug("Factory before load: ${it.value.dump()}")
		factory.load(it.value)
        logger.debug("Factory after load: ${factory.dump()}")
		//println "factory after load: " +
		factory.create()
	} catch (Exception e) {
		println e.dump()
	}
}
