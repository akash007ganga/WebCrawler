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
import java.util.List;

public abstract interface Downloader
{
  public abstract void download(List<String> paramList)
    throws IOException;
}
