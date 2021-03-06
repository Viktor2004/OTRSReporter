package client;

/**
 * Created by Виктор on 27.12.2016.
 */
// это аннотации, т.е. способ отметить наши классы и методы,
// как связанные с веб-сервисной технологией
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

// говорим, что наш интерфейс будет работать как веб-сервис
@WebService
// говорим, что веб-сервис будет использоваться для вызова методов
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WebServiceInterface {
    // говорим, что этот метод можно вызывать удаленно
    @WebMethod
    public String getHelloString(String name);
}
