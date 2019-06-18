package com.github.qq275860560.common.util;

import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class TokenUtilTest {
	 
	@Test
	public void encrypt() throws Exception {
		String user_id = "123";
		String token = TokenUtil.encrypt(user_id);
		Assert.assertTrue(token != null);
	}

	@Test
	public void decrypt() throws Exception {
		String user_id = "123";
		String token = TokenUtil.encrypt(user_id);
		String user_id2 = TokenUtil.decrypt(token);
		Assert.assertTrue(user_id.equals(user_id2));
	}

}
