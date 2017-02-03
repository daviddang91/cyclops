/*
 * Copyright (c) 2016. Zuercher Hochschule fuer Angewandte Wissenschaften
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
package ch.icclab.cyclops.consume.data.mapping.openstack.events;

import ch.icclab.cyclops.consume.data.mapping.openstack.OpenstackEvent;
import ch.icclab.cyclops.load.Loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Oleksii Serhiienko
 * Updated on: 26-Aug-16
 * Description: This class holds the OpenstackNeutronEvent data
 */
public class OpenstackNeutronEvent extends OpenstackEvent {
    public OpenstackNeutronEvent(String account, String instanceId, String type, String time){
        this.account = account;
        this.instanceId = instanceId;
        this.type = type;
        this.time = time;
    }

    /**
     * @return table
     */
    public  String getTableName() {
        return Loader.getSettings().getOpenstackSettings().getOpenstackEventNeutronTable();
    }

    /**
     * Get fields for point generation
     *
     * @return hashmap
     */
    public Map<String, Object> getFields() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("instanceId", instanceId);
        map.put("account", account);
        map.put("type", type);

        return map;
    }

    public String getDateFormat(){
        return "yyyy-MM-dd HH:mm:ss.SSSSSS";
    }
}