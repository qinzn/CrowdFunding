package junit.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author qzn
 * @create 2019/11/5 11:27
 */
public class TestActiviti {

    ApplicationContext ac = new ClassPathXmlApplicationContext("spring/spring-*.xml");
    ProcessEngine processEngine = (ProcessEngine) ac.getBean("processEngine");

    //创建流程实例
    @Test
    public void test04() {
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println("processInstance="+processInstance);
    }


    //查询部署流程定义
    /*
    act_hi_actinst,
    act_hi_procinst,历史的流程实例表
    act_hi_taskinst,历史的流程任务表
    act_ru_execution,
    act_ru_task
    */
    @Test
    public void test03() {
        System.out.println("processEngine = " + processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> list = processDefinitionQuery.list();

        for (ProcessDefinition processDefinition : list) {
            System.out.println("****************Id"+processDefinition.getId());
            System.out.println("****************Key"+processDefinition.getKey());
            System.out.println("****************"+processDefinition.getName());
            System.out.println("****************"+processDefinition.getVersion());
        }

    }

    //部署流程定义
    @Test
    public void test02() {
        System.out.println("processEngine = " + processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess8.bpmn").deploy();

        System.out.println(deploy);


    }

    //创建流程引擎，创建23张表
    @Test
    public void test01() {

        System.out.println("processEngine = " + processEngine);
    }


}
