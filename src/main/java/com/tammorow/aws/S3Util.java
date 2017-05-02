package com.tammorow.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

/**
 * AWS S3ユーティリティクラス
 *
 * @author Tamotsu.Watanabe
 */
public class S3Util {

    /**
     * AmazonS3Clientをインスタンス化
     *
     * @param accessKey
     * @param accessSecretKey
     * @return AmazonS3Client
     */
    public static AmazonS3Client makeClient(String accessKey, String accessSecretKey) {
        return new AmazonS3Client(new BasicAWSCredentials(accessKey, accessSecretKey));
    }

    /**
     * 削除マーカー付きオブジェクトを全て削除する
     * バージョニング有効時のObject削除は最新バージョンに削除マーカーが挿入されるだけでObjectが削除されるわけではない
     * 完全に削除するためにはObjectの全バージョンを削除する必要がある
     * このメソッドでは削除マーカー付きオブジェクトの全バージョンを削除する
     *
     * @param s3Client
     * @param bucketName
     * @param prefix
     */
    public static void deleteObjectsWithDeleteMarker(AmazonS3Client s3Client, String bucketName, String prefix) {

        VersionListing list = s3Client.listVersions(bucketName, prefix);
        do {
            // 削除マーカー付きオブジェクトKey
            String deleteMarkerKey = "";
            for (S3VersionSummary s : list.getVersionSummaries()) {

                // 削除マーカー付きオブジェクト
                if (deleteMarkerKey.equals(s.getKey())) {
                    s3Client.deleteVersion(bucketName, deleteMarkerKey, s.getVersionId());
                } else {
                    deleteMarkerKey = "";
                }

                if (s.isDeleteMarker()) {
                    deleteMarkerKey = s.getKey();
                    // 削除マーカーのバージョンを削除
                    s3Client.deleteVersion(bucketName, deleteMarkerKey, s.getVersionId());
                }
            }
            list = s3Client.listNextBatchOfVersions(list);
        } while (list.getVersionIdMarker() != null);

    }

}
