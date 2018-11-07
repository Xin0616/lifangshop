package com.enation.app.b2b2c.core.statistics.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cFlowStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.statistics.service.IFlowStatisticsManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;

@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/flowStatistics")
public class B2b2cFlowStatisticsController {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IB2b2cFlowStatisticsManager b2b2cBackendFlowStatisticsManager;
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * 得到总流量统计html 
	 * @return result name
	 */
	@RequestMapping(value="/flow-statistics-html")
	public ModelAndView flowStatisticsHtml(){
		ModelAndView view = new ModelAndView();
		List<Store> storeList = this.storeManager.listAll();
		view.addObject("storeList",storeList);
		view.setViewName("/b2b2c/admin/statistics/flow/flow_statistics");
		return view;	
	}
	
	/**
	 * 得到总流量统计html 
	 * @return result name
	 */
	@RequestMapping(value="/goods-flow-statistics-html")
	public ModelAndView goodsFlowStatisticsHtml(){
		ModelAndView view=new ModelAndView();
		List<Store> storeList = this.storeManager.listAll();
		view.addObject("storeList", storeList);
		view.setViewName("/b2b2c/admin/statistics/flow/goods_flow_statistics");
		return view;	
	}
	
	/**
	 * 获取总流量统计数据
	 * @param startDate 开始时间[可为空]
	 * @param endDate	结束时间[可为空]
	 * @param statistics_type 统计类型[可为空]
	 * @return Json格式的字符串 result = 1 代表成功 否则失败
	 */
	@ResponseBody
	@RequestMapping(value="/get-flow-statistics")
	public Object getFlowStatistics(String start_date, String end_date,String store_id){
		try {
			HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
			String startDateStamp = "";		//开始时间戳
			String endDateStamp = "";		//结束时间戳
			String statisticsType = request.getParameter("statistics_type");	//统计类型 0=按月统计 1=按年统计
			
			// 如果统计类型为空
			if (statisticsType == null || "".equals(statisticsType)) {
				statisticsType = "1";
			}
			
			// 1.判断并赋值
			if (start_date != null && !"".equals(start_date)) {
				startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
			}
			if (end_date != null && !"".equals(end_date)) {
				endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
			}
			
			// 2.获取数据
			List<Map<String, Object>> list = this.b2b2cBackendFlowStatisticsManager.getFlowStatistics(statisticsType, startDateStamp, endDateStamp,store_id);

			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取总流量统计出错", e);
			return JsonResultUtil.getErrorJson("获取总流量统计出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取商品访问流量统计数据
	 * @param startDate 开始时间[可为空]
	 * @param endDate	结束时间[可为空]
	 * @return Json格式的字符串 result = 1 代表成功 否则失败
	 */
	@ResponseBody
	@RequestMapping(value="/get-goods-flow-statistics")
	public Object getGoodsFlowStatistics(String start_date, String end_date,String store_id){
		try {

			int top_num = 30;				//排名名次 默认30
			String startDateStamp = "";		//开始时间戳
			String endDateStamp = "";		//结束时间戳
			
			// 1.判断并赋值
			if (start_date != null && !"".equals(start_date)) {
				startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
			}
			if (end_date != null && !"".equals(end_date)) {
				endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
			}
			
			// 2.获取数据
			List<Map<String, Object>> list = this.b2b2cBackendFlowStatisticsManager.getGoodsFlowStatistics(top_num, startDateStamp, endDateStamp,store_id);

			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取总流量统计出错", e);
			return JsonResultUtil.getErrorJson("获取总流量统计出错:" + e.getMessage());
		}
	}
}