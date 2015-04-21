/*******************************************************************************
 * Licensed to the Software Freedom Conservancy (SFC) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The SFC licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package common;

import org.json.JSONObject;

/**
 * 
 * @author Vishshady
 *
 */
public class VariableStorage {
	private static ThreadLocal<JSONObject> setVar = new ThreadLocal<JSONObject>();

	public static String getVar(String key) {
		JSONObject getKey = setVar.get();
		return getKey.get(key).toString();
	}

	public static void setVar(String key) {
		JSONObject setKey = new JSONObject();
		setKey.put(key, JSONObject.NULL);
		if (setVar.get() == null)
			setVar.set(setKey);
		else
			setVar.get().put(key, JSONObject.NULL);

	}

	public static void setVar(String key, String value) {
		JSONObject setKey = new JSONObject();
		setKey.put(key, value);
		if (setVar.get() == null)
			setVar.set(setKey);
		else
			setVar.get().put(key, value);
	}

}
