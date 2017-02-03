
/*
 * Copyright (c) 2015. Zuercher Hochschule fuer Angewandte Wissenschaften
 *  All Rights Reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may
 *     not use this file except in compliance with the License. You may obtain
 *     a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *     WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *     License for the specific language governing permissions and limitations
 *     under the License.
 */
package ch.icclab.cyclops.schedule.runner.openstack;

import ch.icclab.cyclops.consume.data.mapping.udr.OpenStackUDR;
import ch.icclab.cyclops.consume.data.mapping.udr.OpenStackUpTimeUDR;
import ch.icclab.cyclops.load.Loader;
import ch.icclab.cyclops.persistence.HibernateClient;
import ch.icclab.cyclops.persistence.pulls.LatestPullNova;
import ch.icclab.cyclops.schedule.runner.OpenStackClient;
import ch.icclab.cyclops.util.loggers.SchedulerLogger;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * Author: Oleksii Serhiienko
 * Updated on: 26-Aug-16
 * Description: Runner to generate Nova udr records out of events and send to the queue
 */
public  class NovaUDRRunner extends OpenStackClient {
    public String getDbName() {
        return Loader.getSettings().getOpenstackSettings().getOpenstackEventNovaTable();
    }

    public OpenStackUDR generateValue(Long eventTime, Long eventLastTime, Map lastEventInScope, String instanceId){
        return new OpenStackUpTimeUDR(eventLastTime /1000, lastEventInScope.get("account").toString(),
                instanceId, lastEventInScope.get("type").toString(),
                (double) (eventTime - eventLastTime) /1000, //Seconds instead of milliseconds
                new Double (lastEventInScope.get("cpu").toString()),
                new Double (lastEventInScope.get("memory").toString()));
    }

    public void updateLatestPull(Long time){
        LatestPullNova pull = (LatestPullNova) hibernateClient.getObject(LatestPullNova.class, 1l);
        if (pull == null) {
            pull = new LatestPullNova(time);
        } else {
            pull.setTimeStamp(time);
        }
        SchedulerLogger.log("The last pull set to "+pull.getTimeStamp().toString());
        hibernateClient.persistObject(pull);
    }

    public DateTime getLatestPull(){
        DateTime last;
        LatestPullNova pull = (LatestPullNova) HibernateClient.getInstance().getObject(LatestPullNova.class, 1l);
        if (pull == null) {
            last = new DateTime(0);
        } else {
            last = new DateTime(pull.getTimeStamp());
        }
        return last;
    }
}