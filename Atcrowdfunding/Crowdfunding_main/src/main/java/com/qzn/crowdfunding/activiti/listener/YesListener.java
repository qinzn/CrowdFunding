package com.qzn.crowdfunding.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author qzn
 * @create 2019/11/7 17:30
 */
//流程监听器
public class YesListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("审批通过");
    }
}
