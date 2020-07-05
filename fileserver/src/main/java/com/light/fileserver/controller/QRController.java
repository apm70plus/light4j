package com.light.fileserver.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 二维码API
 * @author liuyg
 */
@Api(tags = {"二维码API" })
@RestController
public class QRController {

	private static final int QR_WIDTH = 300;
	private static final String QR_FILE_TYPE = "png";
	
    @ApiOperation(value = "生成二维码图片，服务端不存储")
    @RequestMapping(value = "/api/qrcode/image", method = RequestMethod.GET)
	public void createQrWithoutSave(
			@RequestParam(value="width", required=false, defaultValue = "0") int width,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, WriterException {
		BitMatrix matrix = createQrMatrix(width, httpServletRequest);
		MatrixToImageWriter.writeToStream(matrix, QR_FILE_TYPE, httpServletResponse.getOutputStream());
	}
    
    @ApiOperation(value = "生成二维码Base64图片，服务端不存储")
    @RequestMapping(value = "/api/qrcode/base64Image", method = RequestMethod.GET)
	public void createQrBase64WithoutSave(
			@RequestParam(value="width", required=false, defaultValue = "0") int width,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, WriterException {
		BitMatrix matrix = createQrMatrix(width, httpServletRequest);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, QR_FILE_TYPE, out);
		byte[] base64 = Base64Utils.encode(out.toByteArray());
		httpServletResponse.getOutputStream().write(base64);
	}

	private BitMatrix createQrMatrix(int width, HttpServletRequest httpServletRequest)
			throws IOException, UnsupportedEncodingException, WriterException {
		InputStream in = httpServletRequest.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		String body = out.toString("UTF-8");
		int qr_width = width > 0 ? width : QR_WIDTH;
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 设置编码类型为utf-8
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置二维码纠错能力级别为Q
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
		BitMatrix matrix = new MultiFormatWriter().encode(body, BarcodeFormat.QR_CODE, qr_width, qr_width, hints);
		return matrix;
	}
}
