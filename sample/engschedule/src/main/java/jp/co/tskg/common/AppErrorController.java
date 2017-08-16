package jp.co.example.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * 共通エラー処理
 * 例外があがった場合は必ずここを通る
 *
 * @author user01
 *
 */
@Slf4j
@Controller
public class AppErrorController implements ErrorController {
	private static final String PATH = "/error";

	@Autowired
	AppSettings settings;

	@Autowired
	ErrorAttributes errorAttributes;

    @Value("${debug:false}")
    boolean debug;

	@Override
	public String getErrorPath() {
		return PATH;
	}

	/**
	 * ブラウザから呼び出された場合のエラー処理
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value =PATH, produces = "text/html")
	public String errorHtml(HttpServletRequest req,HttpServletResponse res) {
		// Map<String, Object> attattributes = exceptionAnalysis(req, res);
		// return new ModelAndView("error/general",attattributes);
		exceptionAnalysis(req, res); // ログ出力のみ実施し、単純にリダイレクトする
		return "redirect:" + settings.getErrorRedirectTarget();

	}

	/**
	 * REST API から呼び出された場合のエラー処理
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value =PATH, produces = "application/json")
	public Map<String,Object> errorJson(HttpServletRequest req,HttpServletResponse res) {
		Map<String, Object> attattributes = exceptionAnalysis(req, res);
		return attattributes;
	}

    /**
     * 例外に含まれるステータスコードとメッセージを返す
     *
     * @param req
     * @param res
     * @return
     */
	private Map<String, Object> exceptionAnalysis(HttpServletRequest req, HttpServletResponse res) {
		RequestAttributes requestAttributed = new ServletRequestAttributes(req);
		Map<String,Object> attattributes = errorAttributes.getErrorAttributes(requestAttributed, debug);
		Throwable cause = errorAttributes.getError(requestAttributed);
		if (cause instanceof AppException) {
			log.error(AppLogMessageConstants.INTENTIONAL_ERROR.getMessage());
			// 独自例外を投げられた場合の処理
			AppException appException = (AppException)cause;
			res.setStatus(appException.getStatus());
            attattributes.put("status",appException.getStatus());
            attattributes.put("error",HttpStatus.valueOf(appException.getStatus()).getReasonPhrase());
            attattributes.put("message",appException.getMessage());
		} else {
			log.error(AppLogMessageConstants.UNINTENTIONAL_ERROR.getMessage());
		}
		return attattributes;
	}
}
