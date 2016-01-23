module.exports = function (grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        names: {
            projectName: '<%= pkg.name %>',
            devPage: 'dev.html',
            prodPage: 'index.html',
            gruntTarget: 'target/grunt',
            webrootTarget: 'target/classes/webroot',
            gruntProjectTarget: '<%= names.gruntTarget %>/<%= names.projectName %>',
            gruntDevPage: '<%= names.gruntProjectTarget %>/<%= names.devPage %>',
            gruntProdPage: '<%= names.gruntProjectTarget %>/<%= names.prodPage %>',
            webrootProjectTarget: '<%= names.webrootTarget %>/<%= names.projectName %>',
            webrootDevPage: '<%= names.webrootProjectTarget %>/<%= names.devPage %>',
            webrootProdPage: '<%= names.webrootProjectTarget %>/<%= names.prodPage %>',
        },

        clean: ["<%= names.gruntTarget %>", '.tmp'],

        copy: {
            dev: {
                src: '<%= names.webrootProdPage %>',
                dest: '<%= names.webrootDevPage %>'
            },
            main: {
                expand: true,
                cwd: "<%= names.webrootTarget %>",
                src: ['**'],
                dest: '<%= names.gruntTarget %>'
            },
            prod: {
                src: '<%= names.gruntProdPage %>',
                dest: '<%= names.webrootProdPage %>'
            }

        },

        useminPrepare: {
            html: '<%= names.gruntDevPage %>'
        },

        rev: {
            files: {
                src: ['<%= names.gruntTarget %>/**/*.{js,css}']
            }
        },

        usemin: {
            html: '<%= names.gruntDevPage %>'
        },

        uglify: {
            options: {
                ASCIIOnly: false // actually not necessary
            }
        },

        inline: {
            dist: {
                options:{
                    tag: '',
                    uglify: true
                },
                src: '<%= names.gruntDevPage %>',
                dest: '<%= names.gruntProdPage %>'
            }
        },

        replace: {
          dist: {
            src: '<%= names.gruntProdPage %>',
            overwrite: true,
            replacements: [{
              from: '\'";</script>',
              to: '\'";\\x3c/script>'
            }]
          }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    //grunt.loadNpmTasks('grunt-contrib-concat');
    //grunt.loadNpmTasks('grunt-contrib-cssmin');
    //grunt.loadNpmTasks('grunt-contrib-uglify');
    //grunt.loadNpmTasks('grunt-rev');
    //grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-inline');
    grunt.loadNpmTasks('grunt-text-replace');

    // Tell Grunt what to do when we type "grunt" into the terminal
    grunt.registerTask('default', [
        //'clean', 'copy', 'useminPrepare', 'concat:generated', 'uglify:generated', 'inline'
        'clean', 'copy:dev', 'copy', 'inline', 'replace', 'copy:prod'
        //'copy', 'useminPrepare', 'concat'/*, 'uglify', 'cssmin', 'rev'*/, 'usemin'
    ]);
};