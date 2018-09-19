package com.qaprosoft.jenkins.pipeline.carina
import com.qaprosoft.jenkins.pipeline.Runner2

import groovy.transform.InheritConstructors

@InheritConstructors
public class CarinaRunner extends Runner2 {

    public CarinaRunner(context) {
        super(context)
    }

	public void onPush() {
		context.println("CarinaRunner->onPush")
		//scanner.scanRepository()
		// handle each push/merge operation
		// execute logic inside this method only if $REPO_HOME/Jenkinsfile was updated
//		context.println("TODO: implement snapshot build generation and emailing build number...")
	}
	
	public void onPullRequest() {
		context.println("CarinaRunner->onPullRequest")
	}
}