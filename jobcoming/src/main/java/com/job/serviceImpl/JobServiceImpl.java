package com.job.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job.bean.Job;
import com.job.bean.JobKind;
import com.job.dao.JobDao;
import com.job.dao.JobKindDao;
import com.job.service.JobService;
import com.job.utils.TimeUtils;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	private JobDao jobDao;
	@Autowired
	private JobKindDao jobKindDao;
	@Override
	public List<Job> getJobs(Map<String, Object> map) {
		
		List<Job> list = new ArrayList<Job>();
		
		if(map!=null&&map.size()>0){
			if(map.containsKey("weekTime")&&map.get("weekTime")!=null){
				List<Job> tempList = new ArrayList<Job>();
				int dayofweek = (int)map.get("weekTime");
				list = jobDao.searchJobs(map);
				for(int index = 0;index<list.size();index++){
					Job job = list.get(index);
					if(TimeUtils.getWeek(job.getJobTime())==dayofweek){
						tempList.add(job);						
					}
				}
				System.out.println(list.size());
				return tempList;
			}
						
			if(map.containsKey("pageIndex")){

				Object obj = map.get("pageIndex");				
				Integer pageIndex = (Integer)obj;					
				Integer beginIndex = (pageIndex-1)*10;
				Integer lastIndex  = (Integer)obj + 10;
								
				map.put("beginIndex", beginIndex);
				map.put("lastIndex", lastIndex);				
				list = jobDao.searchJobs(map);
	
			}else{
				//默认每个十个，暂时用常量代替
				map.put("pageSize", 10);
				list = jobDao.searchJobs(map);
			}
			
			

		}
		
		return list;
	}

	@Override
	public List<JobKind> getJobKinds() {
		List<JobKind> list = new ArrayList<JobKind>();
		
		list = jobKindDao.getAllJobKind();
		
		return list;
	}

	@Override
	public Job getJobById(int jobId) {
		if(jobId!=0){
			Job job=new Job();
			job=jobDao.getJobByJobId(jobId);
			return job;
		}else{
			return null;
		}
		
	}

	@Override
	public boolean addJob(Job job) {
		if(job!=null){
			jobDao.addJob(job);
			return true;
		}
		return false;
	}

	@Override
	public List<Job> getJobListByUserId(int userId,int beginIndex,int endIndex) {
		List<Job> list=new ArrayList<Job>();
		list=jobDao.getJobsByUserId(userId,beginIndex,endIndex);
		return list;
	}

}
