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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Concrete implementation of the download service which will prepare absolute url address 
 * for the requested downloadable resources
 */
public class MailCrawlerService implements WebCrawlerService {
	private static final Logger log = Logger.getLogger(MailCrawlerService.class);
	private String searchToken = "";

	public MailCrawlerService(String searchText) {
		this.searchToken = searchText;
	}

	public List<String> getURLList(String startURL) throws IOException {
		return getURLList(Jsoup.connect(startURL).get(), this.searchToken);
	}

	public List<String> getURLList(Document doc, String searchToken) throws IOException {
		Elements linkElements = getLinkElements(doc, "a");
		CollectionUtils.filter(linkElements, getLinkFilterPredicate(getShouldVisitPattern(searchToken)));
		return getAbsoluteMailUrls(linkElements, searchToken);
	}

	/**
	 * This method will create the filter criteria for the URL. 
	 *
	 * @param year the year
	 * @return the should visit pattern
	 */
	public String getShouldVisitPattern(String year) {
		return "(http://)?[a-zA-Z-._/]+" + year + "[0-9]{2}[.a-z/]+thread";
	}

	/**
	 * Gets the regex mail url pattern.
	 *
	 * @param year the year
	 * @return the regex mail url pattern
	 */
	public String getRegexMailUrlPattern(String year) {
		return "(http://)?[a-zA-Z-._/]+" + year + "[0-9]{2}[.a-z]+/%[a-zA-Z0-9-._@%\\s]+";
	}

	/**
	 * Gets the link filter predicate.
	 *
	 * @param shouldVisitPattern the should visit pattern
	 * @return the link filter predicate
	 */
	private Predicate getLinkFilterPredicate(final String shouldVisitPattern) {
		return new Predicate() {
			public boolean evaluate(Object arg0) {
				Pattern pattern = Pattern.compile(shouldVisitPattern);
				Element linkElement = (Element) arg0;
				String absoluteUrl = linkElement.attr("abs:href");
				Matcher matcher = pattern.matcher(absoluteUrl);
				if (matcher.find()) {
					if (MailCrawlerService.log.isDebugEnabled()) {
						MailCrawlerService.log.debug("Should be visited: " + absoluteUrl);
					}
					return true;
				}
				if (MailCrawlerService.log.isDebugEnabled()) {
					MailCrawlerService.log.debug("Should not be visited: " + absoluteUrl);
				}
				return false;
			}
		};
	}

	/**
	 * Gets the absolute mail urls.
	 *
	 * @param linkElements the link elements
	 * @param searchToken the search token
	 * @return the absolute mail urls
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<String> getAbsoluteMailUrls(Elements linkElements, String searchToken) throws IOException {
		List<String> absoluteURLList = new ArrayList<String>();
		List<Element> relativeURLList = new ArrayList<Element>();
		for (Element linkElement : linkElements) {
			String absouleUrl = linkElement.attr("abs:href");
			Elements anchorElements = getLinkElements(Jsoup.connect(absouleUrl).get(), "a");
			CollectionUtils.select(anchorElements, getLinkFilterPredicate(getRegexMailUrlPattern(searchToken)),
					relativeURLList);
		}
		for (Element element : relativeURLList) {
			absoluteURLList.add(element.attr("abs:href"));
		}
		if (log.isDebugEnabled()) {
			log.debug("Absolute URL List: " + absoluteURLList.toString());
		}
		return absoluteURLList;
	}

	/**
	 * Gets the link elements.
	 *
	 * @param doc the doc
	 * @param tagSelector the tag selector
	 * @return the link elements
	 */
	private Elements getLinkElements(Document doc, String tagSelector) {
		return doc.select(tagSelector);
	}
}
