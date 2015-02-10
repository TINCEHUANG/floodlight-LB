/**
 *    Copyright 2013, Big Switch Networks, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License"); you may
 *    not use this file except in compliance with the License. You may obtain
 *    a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 **/

package net.floodlightcontroller.loadbalancer;

import net.floodlightcontroller.packet.IPv4;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Data structure for Load Balancer based on
 * Quantum proposal http://wiki.openstack.org/LBaaS/CoreResourceModel/proposal 
 * 
 * 
 */

@JsonSerialize(using=LBMemberSerializer.class)
public class LBMember implements Runnable{
    protected String id;
    protected int address;
    protected short port;
    protected String macString;
    
    protected double responseTime;
    protected int nConnections;
    protected double cpuUsage;
    protected double memUsage;
        
    protected double new_request_rt_impact;
    protected double new_request_cpu_impact;
    protected double new_request_memory_impact;
    protected double weight;
    protected boolean isOverloaded;
    protected boolean isOutOfService;
    protected int unreportedCount;
    protected double processCapacity;
    
    protected int connectionLimit;
    protected short adminState;
    protected short status;

    protected String poolId;
    protected String vipId;
    
    public LBMember() {
        id = String.valueOf((int) (Math.random()*10000));
        address = 0;
        macString = null;
        port = 0;
        
        responseTime = 0;
        nConnections = 0;
        cpuUsage = 0;
        memUsage = 0;
        new_request_rt_impact = 1;
        new_request_cpu_impact = 0.5;
        new_request_memory_impact = 0.5;
        isOverloaded = false;
        isOutOfService = false;
        unreportedCount = 0;
        weight = 1;
        processCapacity = 1.0;
        
        connectionLimit = 0;
        adminState = 0;
        status = 0;
        poolId = null;
        vipId = null;
      //state monitor for related dynamic feedback algorithm
        
        Thread t = new Thread(this);
        t.start();
    }
  //compute how long or how many times controller haven't receive this server's report
    public void run(){
    	while(true){
    		 try {
 				Thread.sleep(5000);
 			}catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
    		if(unreportedCount >= 10 ){
    			if(isOutOfService == false){
    				System.out.println("Server" + IPv4.fromIPv4Address(this.address) + "break down");
    			isOutOfService = true;
    			}
    			continue;
    		}
    		else{
    		unreportedCount++;
    		System.out.println("Server" + IPv4.fromIPv4Address(this.address) 
    				+ "unreported count is" + unreportedCount);}
           
    	}
    }
}
