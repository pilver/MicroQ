%% @author Simon Evertsson

%% @doc The communication module which acts like a bridge between the state processes and Java.

-module(connection_check).
-export([connection_lost/2]).


-include_lib("eunit/include/eunit.hrl").


%% @doc Detects if the connection with the Java Node has been lost, killing the erlang part and generates an error if that's the case
connection_lost(J_Node, Parent) ->
	Result = net_adm:ping(J_Node),
		if 
			Result =/= pong ->
				io:format(">----------------x Connection lost x----------------<~n"),
				exit(Parent, connection_lost),
				timer:sleep(2000),
				io:format("Terminating...~n"),
				rpc:call(list_to_atom(lists:append("erlcom@", net_adm:localhost())), init, stop, []);
			true ->
				true
		end,
	timer:sleep(1000),
	connection_lost(J_Node, Parent).