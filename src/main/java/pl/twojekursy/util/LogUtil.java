package pl.twojekursy.util;

import org.springframework.data.domain.Page;

import java.util.function.Supplier;

public class LogUtil {
    public static void logPage(Supplier<Page<?>> pageSupplier, String methodName){
        System.out.println("---------------" + methodName + " ---------------------");

        Page<?> page = pageSupplier.get();

        System.out.println("getContent(): " + page.getContent());
        System.out.println("getTotalPages(): " + page.getTotalPages());
        System.out.println("getTotalElements(): "+page.getTotalElements());
        System.out.println("getNumber(): "+page.getNumber());
        System.out.println("getNumberOfElements(): "+page.getNumberOfElements());
        System.out.println("getSize(): "+page.getSize());
    }
}
