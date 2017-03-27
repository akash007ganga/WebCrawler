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

import java.util.List;
import org.apache.log4j.Logger;

public class WebCrawlController {
	private static final Logger log = Logger.getLogger(WebCrawlController.class);

	public static void main(String[] args) throws Exception {
		log.info("Starting web crawling");
		System.out.println("Starting web crawling");

		long startTime = System.currentTimeMillis();
		try {
			CrawlContext ctx = new CrawlContext("crawler.properties");
			WebCrawlerService service = new MailCrawlerService(ctx.getPropertyValue("context.searchtoken"));
			Downloader downloadRunner = new MailDownloader(ctx.getPropertyValue("context.mail.download.location"));
			List<String> absoluteURLList = service.getURLList(ctx.getPropertyValue("context.starturl"));
			downloadRunner.download(absoluteURLList);
			long endTime = System.currentTimeMillis();
			log.info("Time taken in Crawling: " + (endTime - startTime) / 1000L);
			System.out.println("Time taken in Crawling: " + (endTime - startTime) / 1000L);
		} catch (Exception ex) {
			log.error("Exception occurred while crawling: ", ex);
		}
	}
}
