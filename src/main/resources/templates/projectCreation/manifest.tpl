applications:
  - name: {{APPLICATION_NAME}}
{{#IS_NODE_APP}}
    command: npm start
{{/IS_NODE_APP}}
{{#IS_REACT}}
    buildpack: staticfile_buildpack
{{/IS_REACT}}