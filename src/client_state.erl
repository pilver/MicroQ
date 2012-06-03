%% @author Nanna Kjellin Lagerqvist

%% @doc The module representing a student's state

-module(client_state).
-export([init/1, listener/5]).

-include_lib("eunit/include/eunit.hrl").

%%
%% @doc The listener function receives messages from and answers back to jcom (the parent process) and calls itself recursively.
listener(PID, Waiting, State, Mbox, SWpid) ->
	receive
		{swpid, SwPid} ->
			listener(PID, Waiting, State, Mbox, SwPid);
		{Mailbox, ready} ->
			if 
				State =:= ready ->
					%%io:format("~p I am ready~n", [self()]),
					PID ! {Mailbox, client_ready, self()},
					listener(PID, false, ready, Mbox, SWpid);
				true -> 
					%%io:format("~p I am not ready yet~n", [self()]),
					PID ! {Mailbox, not_ready, self()},
					listener(PID, true, State, Mailbox, SWpid)
			end;
		
		switch ->
			if 
				Waiting =:= true ->
					%%io:format("~p I am back from whatever I did and I am ready~n", [self()]),
					PID ! {Mbox, client_ready, self()},
					listener(PID, false, ready, Mbox, SWpid);
			true -> ok
			end,
			if 
				State =:= ready ->
					%%io:format("~p Switching to state 'away'~n", [self()]),
					listener(PID, false, away, Mbox, SWpid);	
				true ->
					%%io:format("~p I'm back!~n", [self()]),
					listener(PID, false, ready, Mbox, SWpid)
			end;
		kill ->
			exit(SWpid, terminated),
			%%io:format("I will now die~n"),
			exit(terminated),
			terminated
	end.

%%
%% @doc The init function sends "client_initialized_successfully" to jcom (the parent process) if the child process started successfully, then calls function listener.
init(PID) ->
	process_flag(trap_exit, true),
	PID ! client_initialized_successfully,
	listener(PID, false, ready, hej, PID).

	




%% ------------------ Eunit Tests ----------------------
   
%%init_test() ->
	%%PID = spawn_link(client_state, init, [self()]),
	%%PID ! {hej, ready},
    %%?assertMatch(no_error, no_error).

listener_test() ->
	PID = spawn_link(client_state, init, [self()]),
	PID ! {hej, ready},
	receive
		{_, client_ready, _} ->
			?assertMatch(true, true)
	end,
	PID ! switch,
	PID ! {hej, ready},
	receive
		{_, not_ready, _} ->
			?assertMatch(true, true)
	end,
	PID ! switch,
	PID ! {hej, ready},
	receive
		{_, client_ready, _} ->
			?assertMatch(true, true)
	end,
    ?assertMatch(tbi, tbi).
	

	

