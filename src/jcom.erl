%% @author Simon Evertsson

%% @doc The communication module which acts like a bridge between the state processes and Java.

-module(jcom).
-export([communicator/2, init/0, loop/1]).


-include_lib("eunit/include/eunit.hrl").

%% @doc Spawns a new state process and returns the PID
spawner(Switch_rate) ->
	PID = spawn_link(client_state, init, [self()]),
	SWpid = spawn_link(state_switcher, init, [Switch_rate, PID]),
	PID ! {swpid, SWpid},
	PID.

%% @doc The communication function which sends and recieves message from its child processes and Java
communicator(Pid_list, Java_node) ->
	receive
		{Mailbox, new_client, Switch_rate} ->
			%%io:format("A new child has spawned!~n"),
			PID = spawner(Switch_rate),
			{Mailbox, Java_node} ! {new_client, PID},
			Updated_pid_list = [PID| Pid_list],
			communicator(Updated_pid_list, Java_node);
		{Mailbox, ready, PID} ->
			PID ! {Mailbox, ready},
			communicator(Pid_list, Java_node);
		{Mailbox, client_ready, PID} ->
			{Mailbox, Java_node} ! {client_ready, PID},
			communicator(Pid_list, Java_node);
		{Mailbox, terminate} ->
			io:format("Terminating... ~n"),
			terminate(Pid_list),
			{Mailbox, Java_node} ! good_bye,
			rpc:call(list_to_atom(lists:append("erlcom@", net_adm:localhost())), init, stop, []);
		{_, kill, PID} ->
			Updated_pid_list = lists:delete(PID, Pid_list),
			PID ! kill,
			%%io:format("Received message to KILL~n"),
			communicator(Updated_pid_list, Java_node)
	end.

%% @doc Sends a kill message to all Pids in Pid_list
terminate([H|T]) ->
	H ! kill,
	terminate(T);
terminate([]) ->
	finished.

	
	
%% @doc Initializes communication with Java.
init() ->
	%%io:format("inside jcom:init()~n"),
	process_flag(trap_exit, true),
	%%net_kernel:start([erlcom]),
	Java_node = list_to_atom(lists:append("javacom@", net_adm:localhost())),
	io:format("Establishing connection...~n"),
	loop(Java_node),
	spawn_link(connection_check, connection_lost, [Java_node, self()]),
	{mbox, Java_node} ! self(),
	communicator([], Java_node).

%% @doc A loop which continue to ping the Java Node every second until a pong is received.
loop(J_Node) ->
	Result = net_adm:ping(J_Node),
	if 
		Result =/= pong ->
			timer:sleep(1000),
			loop(J_Node);
		true ->
			io:format("Connection with Java established...~n")
	end.

%% ------------------ Eunit Tests ----------------------
   
init_test() ->
    ?assertMatch(tbi, tbi).
	
spawner_test() ->
    ?assertMatch(tbi, tbi).
	
communicator_test() ->
    ?assertMatch(tbi, tbi).