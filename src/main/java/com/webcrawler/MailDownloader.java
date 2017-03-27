//$Id: $
//$Revision: $
//$Date: $

/*
 * +=======================================================================+
 * |                                                                       |
 * |          Copyright (C) 2013-2014 Nomura Research Institute, Ltd.      |
 * |                          All Rights Reserved                          |
 * |                                                                       |
 * |    This document is the sole property of Nomura Research Institute,   |
 * |    Ltd. No part of this document may be reproduced in any form or     |
 * |    by any means - electronic, mechanical, photocopying, recording     |
 * |    or otherwise - without the prior written permission of Nomura      |
 * |    Research Institute, Ltd.                                           |
 * |                                                                       |
 * |    Unless required by applicable law or agreed to in writing,         |
 * |    software distributed under the License is distributed on an        |
 * |    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,       |
 * |    either express or implied.                                         |
 * |                                                                       |
 * +=======================================================================+
 */

package com.webcrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class MailDownloader implements Downloader {
	private String downloadsDirectory;
	private static final Logger log = Logger.getLogger(MailDownloader.class);

	public MailDownloader(String downloadsDirectory) {
		this.downloadsDirectory = downloadsDirectory;
	}

	public void download(List<String> absoluteURLList) throws IOException {
		try {
			int count = 1;
			for (String url : absoluteURLList) {
				File directory = new File(this.downloadsDirectory + url.substring(53, 59));
				if (!directory.exists()) {
					directory.mkdir();
				}
				File file = new File(directory, "_mail_" + count + ".txt");

				ReadEmailAndPersistInFile(url, file);

				log.info("Download completed for : " + url);
				log.info("Download remaining for[" + (absoluteURLList.size() - count) + "]items");
				System.out.println("Download remaining for[" + (absoluteURLList.size() - count) + "]items");
				count++;
			}
		} catch (IOException e) {
			log.error("Exception occurred while processing download action: ", e);
			throw e;
		}
	}

	private void ReadEmailAndPersistInFile(String url, File file) throws IOException {
		OutputStream outputStream = new FileOutputStream(file);
		try {
			IOUtils.copy(new URL(url).openConnection().getInputStream(), outputStream);
		} catch (MalformedURLException ex) {
			log.error("Malformed Mails Found: ", ex);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
}
