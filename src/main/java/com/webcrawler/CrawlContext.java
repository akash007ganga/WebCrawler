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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * The Class is responsible for reading crawler.properties file and prepare the crawler accordingly.
 */
public class CrawlContext {
	private static final Logger log = Logger.getLogger(CrawlContext.class);
	private final Properties crawlerProperties;

	public CrawlContext(String propertyFile) throws FileNotFoundException, IOException {
		this.crawlerProperties = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFile);
			if (inputStream != null) {
				this.crawlerProperties.load(inputStream);
			}
		} catch (FileNotFoundException ex) {
			log.error("crawl property file '" + propertyFile + "' not found in the classpath", ex);
			throw new FileNotFoundException("crawl property file '" + propertyFile + "' not found in the classpath");
		}
	}

	public String getPropertyValue(String propName) {
		return this.crawlerProperties.getProperty(propName);
	}
}
