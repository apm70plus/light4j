package com.light.kaptcha.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.code.kaptcha.Producer;
import com.light.kaptcha.IKaptchaResolver;

@SuppressWarnings("serial")
public class ExtendKaptchaServlet extends HttpServlet implements Servlet {
	
	private Producer kaptchaProducer = null;
	private IKaptchaResolver captchaResolver;
	private String scope;
	
	public ExtendKaptchaServlet(IKaptchaResolver captchaResolver, Producer kaptchaProducer, String scope) {
		this.captchaResolver = captchaResolver;
		this.kaptchaProducer = kaptchaProducer;
		this.scope = scope;
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);

		// Switch off disk based caching.
		ImageIO.setUseCache(false);
	}

	/** */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Set to expire far in the past.
		resp.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		resp.setHeader("Pragma", "no-cache");

		// return a jpeg
		resp.setContentType("image/jpeg");

		// create the text for the image
		String capText = this.kaptchaProducer.createText();
		
		this.captchaResolver.setCaptcha(getCaptchaKey(req, resp), scope, capText);

		// create the image with the text
		BufferedImage bi = this.kaptchaProducer.createImage(capText);

		ServletOutputStream out = resp.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);
	}

	private String getCaptchaKey(HttpServletRequest req, HttpServletResponse resp) {
		for(Cookie cookie : req.getCookies()) {
			if ("CAPTCHAID".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		Cookie cookie = new Cookie("CAPTCHAID", UUID.randomUUID().toString().replaceAll("-", ""));
		cookie.setMaxAge(600);
		cookie.setHttpOnly(true);
		resp.addCookie(cookie);
		return cookie.getValue();
	}
	
}