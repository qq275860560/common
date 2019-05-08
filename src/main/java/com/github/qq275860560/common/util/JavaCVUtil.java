package com.github.qq275860560.common.util;

import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.io.File;

import javax.swing.JFrame;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.presets.opencv_objdetect;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class JavaCVUtil {

	public static void main(String[] args) {
		// System.loadLibrary("opencv_java341.dll");

		// 读取原始图片
		Mat image = imread(new File(JavaCVUtil.class.getResource("/bd_logo1.png").getFile()).getAbsolutePath());
		if (image.empty()) {
			System.err.println("加载图片出错，请检查图片路径！");
			return;
		}
		// 显示图片
		imshow("显示原始图像", image);

		// 无限等待按键按下
		waitKey(0);
	}

	public static void main1(String[] args) throws Exception, InterruptedException {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.start(); // 开始获取摄像头数据
		CanvasFrame canvas = new CanvasFrame("摄像头");// 新建一个窗口
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);

		while (true) {
			if (!canvas.isDisplayable()) {// 窗口是否关闭
				grabber.stop();// 停止抓取
				System.exit(2);// 退出
			}
			canvas.showImage(grabber.grab());// 获取摄像头图像并放到窗口上显示， 这里的Frame
												// frame=grabber.grab();
												// frame是一帧视频图像

			Thread.sleep(50);// 50毫秒刷新一次图像
		}
	}

	public static void recordCamera(String outputFile, double frameRate)
			throws Exception, InterruptedException, FrameRecorder.Exception {
		Loader.load(opencv_objdetect.class);
		FrameGrabber grabber = new OpenCVFrameGrabber(0);// 本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
		grabber.start();// 开启抓取器

		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();// 转换器
		IplImage grabbedImage = converter.convert(grabber.grab());// 抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
		int width = grabbedImage.width();
		int height = grabbedImage.height();

		FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
		recorder.setFormat("flv");// 封装格式，如果是推送到rtmp就必须是flv封装格式
		recorder.setFrameRate(frameRate);

		recorder.start();// 开启录制器
		long startTime = 0;
		long videoTS = 0;
		CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		Frame rotatedFrame = converter.convert(grabbedImage);// 不知道为什么这里不做转换就不能推到rtmp
		while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
			rotatedFrame = converter.convert(grabbedImage);
			frame.showImage(rotatedFrame);
			if (startTime == 0) {
				startTime = System.currentTimeMillis();
			}
			videoTS = 1000 * (System.currentTimeMillis() - startTime);
			recorder.setTimestamp(videoTS);
			recorder.record(rotatedFrame);
			Thread.sleep(40);
		}
		frame.dispose();
		recorder.stop();
		recorder.release();
		grabber.stop();

	}

	// 文件
	// public static void main(String[] args)
	// throws Exception, InterruptedException,
	// org.bytedeco.javacv.FrameRecorder.Exception {
	// recordCamera("video.mp4", 25);
	// }

	// // hls流
	public static void main2(String[] args)
			throws Exception, InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception {
		recordCamera("rtmp://list.sectong.com:1935/hls/osx", 25);
	}

	// rtmp 流

	// public static void main(String[] args)
	// throws Exception, InterruptedException,
	// org.bytedeco.javacv.FrameRecorder.Exception {
	// recordCamera("rtmp://list.sectong.com:1935/myapp/osx", 25);
	// }
}
