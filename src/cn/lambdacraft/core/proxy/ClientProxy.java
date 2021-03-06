/** 
 * Copyright (c) LambdaCraft Modding Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.half-life.cn/
 * 
 * LambdaCraft is open-source. It is distributed under the terms of the
 * LambdaCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.lambdacraft.core.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.lwjgl.input.Keyboard;

import cn.lambdacraft.core.CBCMod;
import cn.lambdacraft.core.CBCPlayer;
import cn.lambdacraft.core.client.key.KeyUse;
import cn.lambdacraft.deathmatch.client.renderer.RenderEmptyBlock;
import cn.liutils.core.client.register.LIKeyProcess;
import cn.liutils.core.client.register.LISoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * 客户端代理加载。
 * 
 * @author WeAthFolD, Mkpoli, HopeAsd
 * 
 */
public class ClientProxy extends Proxy {

	LISoundRegistry events = new LISoundRegistry();
	
	private final String PATH_TRACKS[] = { "hla", "hlb", "hlc" } ;
	
	private final int MAX_CROSSHAIR_FILES = 16, MAX_SPRAY_FILES = 14;
	
	@Override
	public void init() {
		super.init();
		events.onSound(new SoundLoadEvent(Minecraft.getMinecraft().sndManager));
		
		CBCPlayer cbcPlayer = new CBCPlayer();
		TickRegistry.registerTickHandler(cbcPlayer, Side.CLIENT);
		RenderingRegistry.registerBlockHandler(new RenderEmptyBlock());
		ClientProps.loadProps(CBCMod.config);
		
		File file;
		URL url;
		final String absPath = "/assets/lambdacraft/";
		String path = getBasePath();
		try {
			for(String s : PATH_TRACKS) {
				url = Minecraft.class.getResource(absPath + "records/" + s + ".ogg");
				if(url != null)
					copyFile(url.openStream(), path + "/assets/records/" + s + ".ogg");
			}
			
			for(int i = 0; i < MAX_CROSSHAIR_FILES; i++) {
				url = Minecraft.class.getResource(absPath + "crosshairs/xhair" + i + ".png");
				if(url != null)
					copyFile(url.openStream(), path + "/assets/lambdacraft/crosshairs/xhair" + i + ".png");
			}
			
			for(int i = 0; i < MAX_SPRAY_FILES; i++) {
				url = Minecraft.class.getResource(absPath + "spray/" + i + ".bmp");
				if(url != null)
					copyFile(url.openStream(), path + "/assets/lambdacraft/sprays/" + i + ".bmp");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(events);
		LIKeyProcess.addKey("key.cbcuse", Keyboard.KEY_F, true, new KeyUse());
		
	}
	
	public static String getBasePath() {
		Minecraft mc = Minecraft.getMinecraft();
		String path = mc.mcDataDir.getAbsolutePath();
		//path = path.substring(0, path.length() - 2);
		return path;
	}
	
	public static String process(URL url) {
		String s = url.getPath();
		int del = s.indexOf('!');
		int ind = s.indexOf("file:/") + 5;
		//if(del == -1)
			//return s;
		System.out.println("ATTEMPT 1 del : " + del + " , ind = " + ind);
		System.out.println("ss : " + s.substring(ind + 1));
		return ind == 0 ? s : s.substring(ind + 1);
		//return s.substring(0, del -1) + s.substring(del + 1);
	}
	

	public static File copyFile(File file, String newPath) { 
		 try {
			return copyFile(new FileInputStream(file), newPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static File copyFile(InputStream inStream, String newPath) { 
	   File file = new File(newPath);
	   CBCMod.log.fine("Attempting copying to " + newPath);
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           if(file.canRead()) {
        	   CBCMod.log.fine("Dest file already exists : " + newPath);
        	   return file;
           }
           if (inStream.available() != 0) { //文件存在时 
        	   if(!file.canWrite())
        		   file.getParentFile().mkdirs();
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
               fs.close();
               CBCMod.log.fine("Successfully copied to " + newPath);
           } else {
        	   CBCMod.log.severe("Didn't find source file.");
           }
       } 
       catch (Exception e) { 
    	   CBCMod.log.severe("Exceptions occured while copying a single file to " + newPath); 
           e.printStackTrace(); 
       } 
       return file;
   } 

	@Override
	public void profilerStartSection(String section) {
		if (isRendering()) {
			Minecraft.getMinecraft().mcProfiler.startSection(section);
		} else {
			super.profilerStartSection(section);
		}
	}

	@Override
	public void profilerEndSection() {
		if (isRendering()) {
			Minecraft.getMinecraft().mcProfiler.endSection();
		} else {
			super.profilerEndSection();
		}

	}

	@Override
	public void profilerEndStartSection(String section) {
		if (isRendering()) {
			Minecraft.getMinecraft().mcProfiler.endStartSection(section);
		}
		super.profilerEndStartSection(section);
	}

}
