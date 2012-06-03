%% @author Simon Evertsson

%% @doc A simple module which sends messages to a client_state-process when it should switch its state.

-module(state_switcher).
-export([init/2, loop/2]).

-include_lib("eunit/include/eunit.hrl").

%% @doc init - Initializes the state switcher
init(Switch_rate, PID) ->
	random:seed(erlang:now()),
    loop(Switch_rate, PID).


%% @doc The switch loop which switches the state of the client_state process PID. A switch only occurs if a randomly generated number between 1-100 is less than or equal to Switch_rate  
loop(Switch_rate, PID) ->
	Rand = random:uniform(100),
	if 
		Rand =< Switch_rate ->
			PID ! switch,
			timer:sleep(1000 + random:uniform(4000));
		true ->
			timer:sleep(1000 + random:uniform(4000)),
			loop(Switch_rate, PID)
	end,
	loop(Switch_rate, PID).

	
	
	
%% ------------------ Eunit Tests ----------------------
   
init_test() ->
	PID = spawn_link(state_switcher, init, [50, self()]),
	?assertMatch(no_error, no_error).
loop_test() ->
	PID = spawn_link(state_switcher, init, [50, self()]),
	receive
		switch ->
			true
	end,
	exit(PID, normal),
	?assertMatch(no_timeout, no_timeout).