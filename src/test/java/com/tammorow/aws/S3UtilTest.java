package com.tammorow.aws;

import org.junit.Test;

/**
 * AWS S3ユーティリティテストクラス
 *
 * @author Tamotsu.Watanabe
 */
public class S3UtilTest {

    private String accessKey = "ACCESS_KEY";
    private String accessSecretKey = "ACCESS_SECRET_KEY";
    private String bucketName = "BUCKET_NAME";
    private String prefix = "PREFIX/";

    @Test
    public void deleteObjectsWithDeleteMarker() {
        S3Util.deleteObjectsWithDeleteMarker(S3Util.makeClient(accessKey, accessSecretKey), bucketName, prefix);
    }
}
