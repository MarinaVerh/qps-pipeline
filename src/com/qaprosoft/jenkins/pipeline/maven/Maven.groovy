package com.qaprosoft.jenkins.pipeline.maven

public class Maven {
	
	public void executeMavenGoals(goals) {
		logger.debug("Maven mixing->executeMavenGoals")
		context.withMaven(
			// Maven installation declared in the Jenkins "Global Tool Configuration"
			maven: 'M3',
			// Maven settings.xml file defined with the Jenkins Config File Provider Plugin
			// Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
			//mavenSettingsConfig: 'settings',
			mavenLocalRepo: '.repository') {
	 
			// Run the maven build
			
			if (context.isUnix()) {
				context.sh "'mvn' -B ${goals}"
			} else {
				context.bat "mvn -B ${goals}"
			}
		}
	}
	
	//TODO: 
/*	public void executeMavenGoals(goals, maven, mavenLocalRepo, mavenSettingsConfig) {
		logger.info("Maven mixing->executeMavenGoals")
		context.withMaven(
			// Maven installation declared in the Jenkins "Global Tool Configuration"
			maven: 'M3',
			// Maven settings.xml file defined with the Jenkins Config File Provider Plugin
			// Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
			//mavenSettingsConfig: 'settings',
			mavenLocalRepo: '.repository') {
	 
			// Run the maven build
			
			if (context.isUnix()) {
				context.sh "'mvn' -B ${goals}"
			} else {
				context.bat "mvn -B ${goals}"
			}
		}
	}*/
}
