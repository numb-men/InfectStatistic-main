import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * InfectStatistic
 * TODO
 *
 * @author 181700141_呼叫哆啦A梦
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        int length = args.length;
        if (length == 0)
            return;
        else if (args[0].equals("list")) {
            ListCommand command = new ListCommand();
            try {
                command.dealParameter(args);
                command.carryOutActions();
            } catch (IllegalException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            System.out.println("不存在" + args[0] + "指令");
        }

    }
}

//当参数或参数值非法时将抛出该异常类
class IllegalException extends Exception {
    // 记录错误原因
    private String message;

    public IllegalException(String tMessage) {
        message = tMessage;
    }

    public String toString() {
        return message;
    }
}

//处理list命令
class ListCommand {
  // 创建包含参数处理类的集合
  private List<AbstractListHandler> handlers = new ArrayList<AbstractListHandler>();

  /**
   * -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
   * 
   * -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
   * 
   * -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件。
   * 
   * -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈
   * ，dead：死亡患者]， 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp,
   * cure】列出疑似患者和治愈患者的情况， 不指定该项默认会列出所有情况。
   * 
   * -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江。
   * 
   * 下面的布尔变量将标识是否已经创建所对应的参数处理类。
   */
  private boolean logIsExist = false;
  private boolean outIsExist = false;
  private boolean dateIsExist = false;
  private boolean typeIsExist = false;
  private boolean provinceIsExist = false;

  /**
   * 处理list命令的各参数，对各个参数初始化其处理类。
   * 
   * @param 用户输入的命令，含list
   * @throws 如果初步解析list命令如：list命令未提供该参数的执行方法 同一参数出现多次，必须存在的参数不存在时将抛出IllegalException
   */
  public void dealParameter(String[] args) throws IllegalException {
      int l = args.length;
      // 存储参数值
      String[] parameterValues;
      for (int i = 1; i < l; i++) {
          switch (args[i]) {
          case "-log":
              if (logIsExist)
                  throw new IllegalException("错误，重复出现 -log参数");
              if (i == l - 1 || args[i + 1].charAt(0) == '-')
                  throw new IllegalException("错误，未提供-log参数值");
              // 创建处理log参数的对象
              logIsExist = true;
              parameterValues = new String[1];
              parameterValues[0] = args[++i];
              handlers.add(new LogHandler(parameterValues));
              break;
          case "-out":
              if (outIsExist)
                  throw new IllegalException("错误，重复出现-out参数");
              if (i == l - 1 || args[i + 1].charAt(0) == '-')
                  throw new IllegalException("错误，未提供-out参数值");
              // 创建处理out参数的对象
              outIsExist = true;
              parameterValues = new String[1];
              parameterValues[0] = args[++i];
              handlers.add(new OutHandler(parameterValues));
              break;
          case "-date":

              break;
          case "-type":

              break;
          case "-province":

              break;
          default:
              if (args[i].charAt(0) == '-')
                  throw new IllegalException("错误，list命令不支持" + args[i] + "参数");
              throw new IllegalException("错误，参数值" + args[i] + "非法");
          }// end of switch
      } // end of for
      if (!logIsExist && !outIsExist)
          throw new IllegalException("错误，参数-log及-out要求必须存在");
  }

  // 执行各参数所要求的操作
  public void carryOutActions() throws Exception {
      for (AbstractListHandler handler : handlers) {
          handler.handle();
      }
  }

}

//处理list命令各参数的类的基类
abstract class AbstractListHandler {
  // 记录参数值
  protected String[] parameterValues;

  // 判断参数值是否合法
  // public abstract boolean isLegal();
  // 处理该参数所要求的操作
  public abstract void handle() throws Exception;
}

//处理list命令log参数的类
class LogHandler extends AbstractListHandler {
  public LogHandler(String[] tParameterValues) {
      parameterValues = tParameterValues;
  }

  /**
   * 处理-log参数所要求的操作
   * 
   * @throws Exception
   */
  public void handle() throws Exception {
      File file = new File(parameterValues[0]);
      if (!file.exists() && file.isDirectory())
          throw new IllegalException("错误，该日志目录不存在");
  }
}

//处理list命令out参数的类
class OutHandler extends AbstractListHandler {
    public OutHandler(String[] tParameterValues) {
        parameterValues = tParameterValues;
    }

    /**
     * 
     */
    public void handle() throws Exception {

    }
}


