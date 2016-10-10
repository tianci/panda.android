package panda.android.demo.filetest;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;

/**
 * Created by admin on 2016/9/6.
 */
public class UniversalImageLoaderImagePath implements FileNameGenerator {

    @Override
    public String generate(String imageUri) {
        return String.valueOf(imageUri.hashCode()+".apk");
    }
}
