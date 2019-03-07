package com.qaprosoft.jenkins.pipeline.sbt

import com.qaprosoft.Utils
import com.qaprosoft.jenkins.pipeline.Configuration
import com.qaprosoft.scm.github.GitHub
import com.qaprosoft.jenkins.pipeline.AbstractRunner
import java.util.Date
import groovy.transform.InheritConstructors
import java.text.SimpleDateFormat


@InheritConstructors
class SBTRunner extends AbstractRunner {


    def date = new Date()
    def sdf = new SimpleDateFormat("yyyyMMddHHmmss")
    String curDate = sdf.format(date)
    String randomArchiveName = "loadTestingReports" + curDate + ".zip"

    public SBTRunner(context) {
        super(context)
        scmClient = new GitHub(context)
    }

    @Override
    public void build() {
        logger.info("SBTRunner->runJob")
        context.node("performance") {

            context.wrap([$class: 'BuildUser']) {
                try {
                    context.timestamps {

                        context.env.getEnvironment()

                        scmClient.clone()

                        def sbtHome = context.tool 'SBT'

                        def args = Configuration.get("args")

                        context.timeout(time: Integer.valueOf(Configuration.get(Configuration.Parameter.JOB_MAX_RUN_TIME)), unit: 'MINUTES') {
                            context.sh "${sbtHome}/bin/sbt ${args}"
                        }

                    }
                } catch (Exception e) {
                    logger.error(Utils.printStackTrace(e))
                    throw e
                } finally {
                    publishJenkinsReports()
                    clean()
                    uploadResultsToS3()
                    publishResultsInSlack()
                }
            }
        }
    }

    @Override
    public void onPush() {
        //TODO: implement in future
    }

    @Override
    public void onPullRequest() {
        //TODO: implement in future
    }

    protected void publishJenkinsReports() {
        def mails = Configuration.get("mails").toString()
        context.stage('Results') {
            context.gatlingArchive()
            context.zip archive: true, dir: 'target/gatling/', glob: '', zipFile: randomArchiveName
            context.emailext body: 'Test Text', subject: 'Test', to: mails

        }
    }

    protected void clean() {
        context.stage('Wipe out Workspace') {
            context.deleteDir()
        }
    }

    protected void uploadResultsToS3() {
        def needToUpload = Configuration.get("needToUpload").toString().toBoolean()
        if (needToUpload) {
            context.build job: 'loadTesting/Upload-Results-To-S3', wait: false
        }
    }

    protected void publishResultsInSlack() {
        context.build job: 'loadTesting/Publish-Results-To-Slack', wait: false
    }
}
