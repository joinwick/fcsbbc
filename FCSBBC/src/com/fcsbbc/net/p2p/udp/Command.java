package com.fcsbbc.net.p2p.udp;

public enum Command {
	command("进入命令模式"),
	exit_command("退出命令模式"),
	restart("重启"),
	start("启动"),
	stop("断开连接"),
	get_all_client_info("得到所有在线节点信息"),
	request_server("请求服务器，发送自己的信息"),
	send_all_client("给所有在线节点发送自己的信息"),
	send_user_info("给特定在线节点发送自己的信息｛格式： send_user_info:目的IP:端口｝"),
	get_command_list("获取命令列表");
	
	private static boolean isCommandMode = false;
	private String description;

	private Command(String description){
		this.description = description;
	}
	
	public static String getAllCommandDescription(){
//		System.out.println("命令解析!");
		StringBuffer sb = new StringBuffer("命令列表：\r\n");
		Command[] command = Command.values();
		for (int i = 0; i < command.length; i++) {
			Command str = command[i];
			sb.append(str.name() + ":").append(str.description).append("\r\n");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean getIsCommandMode(){
		return isCommandMode;
	}
	/**
	 * 处理命令 
	 * @param cmd
	 * @return
	 */
	public static String  proCommand(String cmd){
		//1.首先输入command 进行命令模式
		if(cmd.equals(Command.command.name())){
			isCommandMode = true;
			return "进入命令模式";
		}
		//1.1 .输入command 退出命令模式
		if(cmd.equals(Command.exit_command.name())){
			isCommandMode = false;
			return "退出命令模式";
		}
		
		if(isCommandMode){
			//2.识别命令，进行处理
			return doCommand(cmd);
		}
		
		return cmd;
	}
	
	/**
	 * 命令处理
	 * @param cmd
	 * @return 0 命令错误
	 */
	private static  String  doCommand(String cmd) {
		try {
			if(cmd.contains("send_user_info")){
				String[] send_user_info_cmd = cmd.split(":");
				cmd = send_user_info_cmd[0];
			}
			switch (Command.valueOf(cmd)) {
			case restart:
				return "1";
			case get_all_client_info:
				return "2";
			case request_server:
				return "3";
			case send_all_client:
				return "4";
			case send_user_info:
				return "5";
			case get_command_list:
				return getAllCommandDescription();
			default:
				return "0";
			}
		} catch (Exception e) {
			return "错误：命令错误，请输入{exit_command}命令退出命令模式！";
		}
		
	}
	
	public static void main(String[] args) {
		String[] commandList = {"command", "exit_command", "start", "restart", "stop", "get_all_client_info", "request_server", "send_all_client",
				"send_user_info", "get_command_list"};
		System.out.println(proCommand("command"));
		
		System.out.println(proCommand(commandList[commandList.length - 1]));
		
		System.out.println(proCommand("send_user_info:172.19.96.83:8103"));
		
		System.out.println(Command.get_command_list.name());
		
//		System.out.println(Command.values().toString());
//		Command[] command = Command.values();
//		for (int i = 0; i < command.length; i++) {
//			Command str = command[i];
//			System.out.print(str.name() +":");
//			System.out.println(str.description);
//		}
//		System.out.println(getAllCommandDescription());
	}
}
